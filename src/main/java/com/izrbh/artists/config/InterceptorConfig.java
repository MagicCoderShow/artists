package com.izrbh.artists.config;

import com.izrbh.artists.interceptor.PermissionInterceptor;
import com.izrbh.artists.service.IPermissionService;
import com.izrbh.artists.service.IRoleService;
import com.izrbh.artists.service.IUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    IUserService userService;
    @Resource
    IRoleService roleService;
    @Resource
    IPermissionService permissionService;
    @Resource
    PermissionInterceptor permissionInterceptor;

    /**
     * 自定义拦截规则
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns - 用于添加拦截规则
        // excludePathPatterns - 用户排除拦截
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }
}