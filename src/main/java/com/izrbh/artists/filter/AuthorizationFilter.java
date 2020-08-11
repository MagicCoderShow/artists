package com.izrbh.artists.filter;

import cn.hutool.json.JSONUtil;
import com.izrbh.artists.jwt.JwtUtil;
import com.izrbh.artists.properties.SystemConsts;
import com.izrbh.artists.tools.JsonResult;
import com.izrbh.artists.tools.ResultCode;
import com.izrbh.artists.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * AuthorizationFilter 登录验证过滤器
 */
@Configuration// 将此Filter交给Spring容器管理
@WebFilter(urlPatterns = "/", filterName = "AuthorizationFilter")
@Order(2)// 指定过滤器的执行顺序，值越大越靠后执行
public class AuthorizationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/user/login", "/user/logout", "/user/register","/authorization/refresh")));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //获取Authorization
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ServletContext sc = request.getSession().getServletContext();
        WebApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(sc);
        JwtUtil jwtUtil = (JwtUtil)cxt.getBean("jwtUtil");
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        boolean allowedPath = ALLOWED_PATHS.contains(path);
        //如果不是非权限访问接口，需要进行登录验证
        if (!allowedPath) {
            //验证jwt-token是否有效
            String authorization = request.getHeader(SystemConsts.JWT_HEADER);
            if (StringUtils.isNotBlank(authorization) && !jwtUtil.verify(authorization,SystemConsts.JWT_TYPE_ACCESS)) {
                outResult(servletResponse,new JsonResult(false, ResultCode.USER_NOT_LOGIN));
                return;
            }
            //判断token是否还在缓存中（因为token过期时间前可能已经被业务逻辑置为过期了）
            String userId = jwtUtil.getClaim(authorization,SystemConsts.JWT_USERID);
            RedisUtil redisUtil = (RedisUtil)cxt.getBean("redisUtil");
            Object cacheToken = redisUtil.get(SystemConsts.PREFIX_ACCESS_TOKEN_CACHE +userId);
            if(cacheToken==null||StringUtils.isBlank(cacheToken.toString())||!cacheToken.equals(authorization)){
                outResult(servletResponse,new JsonResult(false, ResultCode.USER_NOT_LOGIN));
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    /**
     * 输出结果
     * @param response
     * @param result
     */
    private void outResult(ServletResponse response,JsonResult result) throws IOException{
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.append(JSONUtil.toJsonStr(result));
    }
}
