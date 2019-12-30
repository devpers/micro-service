package com.demo.config.filter;

import com.demo.exception.DemoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

/**
 * 过滤器
 * <p>
 * servlet 属于apache - servletApi不在jdk下
 *
 * @author cc zhao 2019/08/10
 */
@WebFilter(urlPatterns = "/new", filterName = "globalFilter")
public class DemoFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(DemoFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            logger.info("demo filter init");
        } catch (Exception e) {
            throw new DemoException("服务繁忙，请稍后再试");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            logger.info("demo filter doFilter");
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new DemoException("服务繁忙，请稍后再试");
        }
    }

    @Override
    public void destroy() {
        logger.info("demo filter destroy");
    }
}
