package com.izrbh.artists.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.izrbh.artists.Exception.CustomException;
import com.izrbh.artists.entity.User;
import com.izrbh.artists.jwt.JwtUtil;
import com.izrbh.artists.mapper.UserMapper;
import com.izrbh.artists.properties.JwtProperties;
import com.izrbh.artists.service.IPermissionService;
import com.izrbh.artists.service.IRoleService;
import com.izrbh.artists.service.IUserService;
import com.izrbh.artists.properties.SystemConsts;
import com.izrbh.artists.tools.ResultCode;
import com.izrbh.artists.utils.RedisUtil;
import com.izrbh.artists.utils.md5.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Desc 用户表 服务实现类
 * @Author xuping
 * @Date 2020-08-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    UserMapper userMapper;
    @Resource
    IRoleService roleService;
    @Resource
    IPermissionService permissionService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private JwtProperties jwtProperties;

    @Override
    public User getUserById(Integer userId, String... columns) {
        return userMapper.selectOne(new QueryWrapper<User>().select(columns).eq("id", userId));
    }

    @Override
    public User getUserByUserName(String userName, String... columns) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        if (columns.length > 0) {
            queryWrapper.select(columns);
        }
        queryWrapper.eq("username", userName);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Map login(String username, String password) throws Exception {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new CustomException(ResultCode.USER_CREDENTIALS_ERROR);
        }
        User user = null;
        try {
            user = getUserByUserName(username);
        } catch (Exception e) {
            logger.error("根据用户名查询用户失败：", e);
            throw new CustomException(ResultCode.SYSTEM_ERROR);
        }
        if (null == user) {
            throw new CustomException(ResultCode.USER_CREDENTIALS_ERROR);
        }

        if (StringUtils.isBlank(user.getPassword()) || !MD5Utils.MD5Upper(password,SystemConsts.LOGIN_SALT).equals(user.getPassword())) {
            throw new Exception("账号或密码不正确");
        }

        //生成token
        String accessToken = jwtUtil.createToken(user.getId().toString(), user.getUsername(), SystemConsts.JWT_TYPE_ACCESS);
        String refreshToken = jwtUtil.createToken(user.getId().toString(), user.getUsername(), SystemConsts.JWT_TYPE_REFRESH);
        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(refreshToken)) {
            logger.error("登录生成Token失败");
            throw new CustomException(ResultCode.SYSTEM_ERROR);
        }
        //token放入缓存
        boolean accessCache = redisUtil.set(SystemConsts.PREFIX_ACCESS_TOKEN_CACHE + user.getId(), accessToken, jwtProperties.getTokenExpireTime());
        boolean refreshCache = redisUtil.set(SystemConsts.PREFIX_REFRESH_TOKEN_CACHE + user.getId(), refreshToken, jwtProperties.getRefreshTokenExpireTime());
        if (!accessCache||!refreshCache){
            logger.error("Token存入redis服务器失败");
            throw new CustomException(ResultCode.SYSTEM_ERROR);
        }
        //查询角色
        Set<String> rolesSet = roleService.getRoleSet(user.getId());
        //查询用权限
        Set<String> permissionsSet = permissionService.getPermissionSet(user.getId());
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("userId",user.getId());
        result.put("nickName",user.getNickName());
        result.put("accessToken",accessToken);
        result.put("refreshToken",refreshToken);
        result.put("roles",rolesSet);
        result.put("permissions",permissionsSet);
        return result;
    }

    @Override
    public Map refreshToken(String refreshToken) throws Exception {
        if (!jwtUtil.verify(refreshToken,SystemConsts.JWT_TYPE_REFRESH)){
            throw new CustomException(ResultCode.USER_CREDENTIALS_EXPIRED);
        }
        Integer userId = Integer.valueOf(jwtUtil.getClaim(refreshToken,SystemConsts.JWT_USERID));
        //获取redis中的refreshToken，判断是否已经过期
        Object cacheRefreshToken = redisUtil.get(SystemConsts.PREFIX_REFRESH_TOKEN_CACHE+userId);
        if(null==cacheRefreshToken||!refreshToken.equals(cacheRefreshToken.toString())){
            throw new CustomException(ResultCode.USER_CREDENTIALS_EXPIRED);
        }
        String username = jwtUtil.getClaim(refreshToken,SystemConsts.JWT_USERID);
        //生成token
        String newAccessToken = jwtUtil.createToken(userId.toString(), username, SystemConsts.JWT_TYPE_ACCESS);
        String newRefreshToken = jwtUtil.createToken(userId.toString(), username, SystemConsts.JWT_TYPE_REFRESH);
        if (StringUtils.isBlank(newAccessToken) || StringUtils.isBlank(newRefreshToken)) {
            logger.error("登录生成Token失败");
            throw new CustomException(ResultCode.SYSTEM_ERROR);
        }
        //token放入缓存
        boolean accessCache = redisUtil.set(SystemConsts.PREFIX_ACCESS_TOKEN_CACHE + userId, newAccessToken, jwtProperties.getTokenExpireTime());
        boolean refreshCache = redisUtil.set(SystemConsts.PREFIX_REFRESH_TOKEN_CACHE + userId, newRefreshToken, jwtProperties.getRefreshTokenExpireTime());
        if (!accessCache||!refreshCache){
            logger.error("Token存入redis服务器失败");
            throw new CustomException(ResultCode.SYSTEM_ERROR);
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("accessToken",newAccessToken);
        result.put("refreshToken",newRefreshToken);
        return result;
    }

    @Override
    public boolean logut(String token) throws Exception {
        if (!jwtUtil.verify(token,SystemConsts.JWT_TYPE_ACCESS)){
            throw new CustomException(ResultCode.USER_CREDENTIALS_EXPIRED);
        }
        Integer userId = Integer.valueOf(jwtUtil.getClaim(token,SystemConsts.JWT_USERID));
        //获取redis中的token，判断是否已经过期
        Object cacheToken = redisUtil.get(SystemConsts.PREFIX_ACCESS_TOKEN_CACHE+userId);
        if(null==cacheToken||!token.equals(cacheToken.toString())){
            throw new CustomException(ResultCode.USER_CREDENTIALS_EXPIRED);
        }
        //删除token 缓存
        redisUtil.del(SystemConsts.PREFIX_ACCESS_TOKEN_CACHE + userId);
        redisUtil.del(SystemConsts.PREFIX_REFRESH_TOKEN_CACHE + userId);
        return true;
    }
}
