package com.izrbh.artists.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.izrbh.artists.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Desc 用户表 服务类
 * @Author xuping
 * @Date 2020-08-05
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户ID查询用户
     * @param userId
     * @param columns 需要查询的字段
     * @return
     */
    User getUserById(Integer userId,String... columns);

    /**
     * 根据用户名查询用户
     * @param userName
     * @param columns 需要查询的字段
     * @return
     */
    User getUserByUserName(String userName,String... columns);

    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    Map login(String userName, String password) throws Exception;

    /**
     * 刷新token
     * @param refreshToken
     * @return
     * @throws Exception
     */
    Map refreshToken(String refreshToken) throws Exception;

    /**
     * 退出
     * @param token
     * @return
     * @throws Exception
     */
    boolean logut(String token) throws Exception;

}
