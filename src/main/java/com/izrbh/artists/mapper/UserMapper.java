package com.izrbh.artists.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.izrbh.artists.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Desc 用户表 Mapper 接口
 * @Author xuping
 * @Date 2020-08-05
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 自定义分页查询
     * @param page 分页参数
     * @param queryWrapper 条件查询
     * @return List<User>
     */
    List<User> selectMyPage(IPage<User> page, @Param(Constants.WRAPPER) Wrapper<User> queryWrapper);
}
