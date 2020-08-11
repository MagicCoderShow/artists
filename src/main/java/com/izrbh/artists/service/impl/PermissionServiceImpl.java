package com.izrbh.artists.service.impl;

import com.izrbh.artists.entity.Permission;
import com.izrbh.artists.mapper.PermissionMapper;
import com.izrbh.artists.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Desc 权限表 服务实现类
 * @Author xuping
 * @Date 2020-08-06
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Resource
    PermissionMapper permissionMapper;

    @Override
    public Set<String> getPermissionSet(Integer userId) {
        List<Permission> permissions = permissionMapper.selectPermissionListByRoleId(userId);
        Set<String> permissionCodeSet = new HashSet<String>();
        permissions.stream().forEach(p -> {permissionCodeSet.add(p.getCode());});
        return permissionCodeSet;
    }
}
