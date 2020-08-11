package com.izrbh.artists.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.izrbh.artists.entity.Permission;

import java.util.Set;

/**
 * @Desc 权限表 服务类
 * @Author xuping
 * @Date 2020-08-06
 */
public interface IPermissionService extends IService<Permission> {
    /**
     * 根据用户ID查询权限编码集合
     * @param userId 用户ID
     * @return
     */
    Set<String> getPermissionSet(Integer userId);

}
