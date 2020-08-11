package com.izrbh.artists.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.izrbh.artists.entity.Permission;
import com.izrbh.artists.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Desc 权限表 Mapper 接口
 * @Author xuping
 * @Date 2020-08-06
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 根据角色ID查询所有角色
     *
     * @param roleId
     * @return
     */
    List<Permission> selectPermissionListByRoleId(Integer roleId);

    /**
     * 根据用户ID查询所有角色
     *
     * @param userId
     * @return
     */
    List<Permission> selectPermissionListByUserId(Integer userId);
}
