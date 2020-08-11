package com.izrbh.artists.controller;

import com.izrbh.artists.entity.User;
import com.izrbh.artists.properties.SystemConsts;
import com.izrbh.artists.service.IUserService;
import com.izrbh.artists.tools.JsonResult;
import com.izrbh.artists.tools.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName IndexController
 * @Description 欢迎页
 * @Author xuping
 * @date 2020.08.05 20:46
 */
@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    @Resource
    IUserService userService;

    /**
     * 刷新获取新token
     * @return
     */
    @RequestMapping("/refresh")
    JsonResult refresh(HttpServletRequest request)throws Exception{
        String refreshToken = request.getHeader(SystemConsts.JWT_HEADER);
        if(StringUtils.isBlank(refreshToken)){
            return  new JsonResult(false, ResultCode.COMMON_FAIL);
        }
        return new JsonResult(true,ResultCode.SUCCESS,userService.refreshToken(refreshToken));
    }
}
