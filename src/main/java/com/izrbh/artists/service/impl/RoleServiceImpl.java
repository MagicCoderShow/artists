package com.izrbh.artists.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.izrbh.artists.entity.Role;
import com.izrbh.artists.mapper.RoleMapper;
import com.izrbh.artists.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Desc 用户角色表 服务实现类
 * @Author xuping
 * @Date 2020-08-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    RoleMapper roleMapper;

    @Override
    public Set<String> getRoleSet(Integer userId) {
        List<Role> roles = roleMapper.selectRoleList(new QueryWrapper<Role>().eq("ur.user_id",userId));
        Set<String> roleCodeSet = new HashSet<String>();
        roles.stream().forEach(role -> {roleCodeSet.add(role.getCode());});
        return roleCodeSet;
    }

    @Override
    public List<Role> selectRoleListByUserId(Integer userId) {
        return roleMapper.selectRoleList(new QueryWrapper<Role>().eq("ur.user_id",userId));
    }
}
