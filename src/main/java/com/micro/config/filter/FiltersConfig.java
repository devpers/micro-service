package com.micro.config.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import java.io.IOException;

/**
 * 过滤器
 * 注意多个过滤器的执行顺序
 *
 * @author cc zhao 2019/08/12
 */
@Configuration
public class FiltersConfig {

    @Bean("demoGlobalFilter")
    public Filter filter() {
        return new Filter() {

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
            }

            @Override
            public void destroy() {

            }
        };
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                chain.doFilter(request, response);
            }

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void destroy() {

            }
        });
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("baseFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }


}
