package com.micro.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.micro.annotation.DemoResponse;
import com.micro.config.interceptor.DemoInterceptor;
import com.micro.exception.DemoException;
import com.micro.exception.GlobalException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * web mvc configurer
 *
 * @author cc zhao 2019/08/09
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    @Autowired
    GlobalException globalException;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 添加自定义converter将会关闭spring默认的
        // 使用ali fastJson 替换默认
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        // fastJsonHttpMessageConverter的配置交由fastJsonConfig
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("utf-8"));
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullListAsEmpty);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.TEXT_PLAIN,
                MediaType.APPLICATION_JSON_UTF8
        ));

        converters.add(fastJsonHttpMessageConverter);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 使用extendMessageConverters可以添加的自定义的而不影响默认
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(new HandlerMethodReturnValueHandler() {
            @Override
            public boolean supportsReturnType(MethodParameter returnType) {
                return returnType.hasMethodAnnotation(DemoResponse.class);
            }

            @Override
            public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
                HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
                // response.setContentType("text/html;charset=UTF-8") 不同的文案类型
                response.setContentType("application/json;charset=UTF-8");

                response.setCharacterEncoding("utf-8");

                // 标识响应已经被直接处理(不再调用视图解析器)  与设置contentType相关？，否则因为重新需要viewResolver解析，因为不一致导致无法解析
                mavContainer.setRequestHandled(true);

                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setCode("100");
                baseResponse.setMsg("success");
                baseResponse.setData(returnValue);

                response.getWriter().write(JSONObject.toJSONString(baseResponse));
            }
        });
    }

    @Data
    class BaseResponse {

        private String msg;

        private String code;

        private Object Data;

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        // 定义异常处理（将接管框架默认处理器）
        resolvers.add((request, response, handler, ex) -> {
            try {
                BaseExceptionResponse baseResponse = new BaseExceptionResponse();
                baseResponse.setMessage("system error");
                if (ex instanceof DemoException) {
                    baseResponse.setMessage(ex.getMessage());
                }
                baseResponse.setCode(105);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.setStatus(200);
                response.getWriter().write(JSON.toJSONString(baseResponse));

                // 如果跳转到指定页面需要使用modelAndView
            } catch (Exception e) {
                logger.error("mvc exception");
            }
            return new ModelAndView();
        });
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        // 扩展异常处理器
    }

    @Data
    class BaseExceptionResponse {
        private int code;

        private String message;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // controller 拦截
        registry.addInterceptor(new DemoInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // 静态资源解析
        registry.addResourceHandler("/resource/**").addResourceLocations("/static/**").setCachePeriod(1234567890);
    }




}
