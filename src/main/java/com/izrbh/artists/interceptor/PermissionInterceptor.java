package com.izrbh.artists.interceptor;

import com.izrbh.artists.Exception.CustomException;
import com.izrbh.artists.annotation.RequiresPermissions;
import com.izrbh.artists.jwt.JwtUtil;
import com.izrbh.artists.service.IPermissionService;
import com.izrbh.artists.service.IRoleService;
import com.izrbh.artists.service.IUserService;
import com.izrbh.artists.properties.SystemConsts;
import com.izrbh.artists.tools.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 方法级权限拦截器
 *
 * @Author xuping
 * @date 2020/8/9
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private IUserService userService;

    @Resource
    private IRoleService roleService;

    @Resource
    private IPermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        // 将handler强转为HandlerMethod, 前面已经证实这个handler就是HandlerMethod
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 从方法处理器中获取出要调用的方法
        Method method = handlerMethod.getMethod();
        // 获取出方法上的RequiresPermissions注解
        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions == null) {
            // 如果注解为null, 说明不需要拦截, 直接放过
            return true;
        }
        if (requiresPermissions.value().length > 0) {
            // 如果权限配置不为空, 则取出配置值
            Set<String> permissionSet = new HashSet<>();
            Arrays.stream(requiresPermissions.value()).forEach(p -> permissionSet.add(p));

            // 从数据库中查询用户的权限集合，判断是否有交集，有交集证明有权限
            String token = request.getHeader("Authorization");
            if(StringUtils.isNotBlank(token)){
                String userId = jwtUtil.getClaim(token, SystemConsts.JWT_USERID);
                if(StringUtils.isNotBlank(userId)){
                    Set<String> userPerssionSet = permissionService.getPermissionSet(Integer.valueOf(userId));
                    userPerssionSet.retainAll(permissionSet);
                    if (userPerssionSet.size() > 0) {
                        return true;
                    }
                }
            }
        }
        // 拦截之后应该返回公共结果, 这里没做处理
        throw new CustomException(ResultCode.NO_PERMISSION);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // log.info("在Controller请求处理之后进行调用，但是在视图被渲染之前");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // log.info("在整个请求结束之后被调用，也就是在DispatcherServlet渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }
}
