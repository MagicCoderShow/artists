package com.izrbh.artists.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.izrbh.artists.entity.Role;

import java.util.List;
import java.util.Set;

/**
 * @Desc 用户角色表 服务类
 * @Author xuping
 * @Date 2020-08-06
 */
public interface IRoleService extends IService<Role> {

    /**
     * 根据用户ID查询用户角色编码集合
     * @param userId
     * @return
     */
    Set<String> getRoleSet(Integer userId);

    /**
     * 根据用户ID查询用户角色
     * @param userId
     * @return
     */
    List<Role> selectRoleListByUserId(Integer userId);
}
