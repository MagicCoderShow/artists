package com.izrbh.artists.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.izrbh.artists.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Desc 用户角色表 Mapper 接口
 * @Author xuping
 * @Date 2020-08-06
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID查询所有角色
     *
     * @param wrapper
     * @return
     */
    List<Role> selectRoleList(@Param(Constants.WRAPPER) QueryWrapper<Role> wrapper);
}
