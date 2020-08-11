package com.izrbh.artists.filter;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 请求日志
 */
@Configuration// 将此Filter交给Spring容器管理
@WebFilter(urlPatterns = "/", filterName = "LogFilter")
@Order(1)// 指定过滤器的执行顺序，值越大越靠后执行
public class LogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        //为了在post的时候能继续传递
        RequestWrapper request = new RequestWrapper((HttpServletRequest) servletRequest);
        ResponseWrapper response = new ResponseWrapper((HttpServletResponse) servletResponse);

        logger.info("=====================LogFilter前置start=====================>");

        //封装log实体类
        Map<String, String> parameterMap = new LinkedHashMap<>();
        if ("GET".equals(request.getMethod())) {
            parameterMap = ServletUtil.getParamMap(request);
        } else {
            parameterMap = JSONUtil.toBean(ServletUtil.getBody(request), Map.class);
        }
        logger.info("请求来源： =》{}", request.getRemoteAddr());
        logger.info("请求URI：{}", request.getRequestURI());
        logger.info("请求方式：{}", request.getMethod());
        logger.info("请求参数：{}", parameterMap);
        logger.info("=====================LogFilter前置  end=====================>");
        //消耗时间
        long start = System.currentTimeMillis();

        // 执行主体方法start==================================================================
        chain.doFilter(request, response);
        // 执行主体方法  end==================================================================

        //耗时
        long time = System.currentTimeMillis() - start;
        logger.info("=====================LogFilter后置start=====================>");
        byte[] content = response.getContent();
        String resultParams = new String();
        if (content.length > 0) {
            resultParams = new String(content, "UTF-8");
        }
        logger.info("返回值：{}", resultParams);
        logger.info("耗时（毫秒）{}：", time);
        //返回消息 否则前台收不到消息
        servletResponse.getOutputStream().write(resultParams.getBytes());
        logger.info("=====================LogFilter后置  end=====================>");
    }

    @Override
    public void destroy() {
    }
}