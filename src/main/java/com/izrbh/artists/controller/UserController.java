package com.izrbh.artists.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.izrbh.artists.annotation.RequiresPermissions;
import com.izrbh.artists.entity.User;
import com.izrbh.artists.properties.SystemConsts;
import com.izrbh.artists.service.impl.UserServiceImpl;
import com.izrbh.artists.jwt.JwtUtil;
import com.izrbh.artists.tools.JsonResult;
import com.izrbh.artists.tools.ResultCode;
import com.izrbh.artists.utils.RedisUtil;
import com.izrbh.artists.utils.md5.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Desc 用户表 前端控制器
 * @Author xuping
 * @Date 2020-08-05
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    UserServiceImpl userService;

    /**
     * 用户分页查询
     *
     * @param page     分页参数
     * @param userName 姓名
     * @return
     */
    @RequiresPermissions("user:query")
    @PostMapping("/list")
    public JsonResult list(@ModelAttribute Page<User> page, @RequestParam(required = false) String userName) {
        JsonResult result = new JsonResult(true);
        try {
            //查询条件组装
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
            //指定查询返回字段
            queryWrapper.select("id", "username", "password", "nick_name", "last_login_time", "enabled", "not_expired", "account_not_locked", "credentials_not_expired");
            if (StringUtils.isNotBlank(userName)) {
                queryWrapper.eq("username", userName);
            }
            result.setData(userService.page(page, queryWrapper));
            //自定义分页查询
            //result.setData(userService.selectMyPage(page, queryWrapper));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户列表失败：",e);
            result.setSuccess(false);
            result.setCode(ResultCode.COMMON_FAIL.getCode());
            result.setErrorMsg(ResultCode.COMMON_FAIL.getMessage());
        }
        return result;
    }

    @PostMapping("/login")
    public JsonResult login(@RequestBody User user) throws Exception {
        if (null==user||StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            return new JsonResult(false, ResultCode.USER_CREDENTIALS_ERROR);
        }
        return new JsonResult(true, userService.login(user.getUsername(),user.getPassword()));
    }

    @PostMapping("/logout")
    public JsonResult logout(HttpServletRequest request) throws Exception {
        String token = request.getHeader(SystemConsts.JWT_HEADER);
        if(StringUtils.isBlank(token)){
            return  new JsonResult(false, ResultCode.COMMON_FAIL);
        }
        userService.logut(token);
        return new JsonResult(true,ResultCode.SUCCESS);
    }

}

