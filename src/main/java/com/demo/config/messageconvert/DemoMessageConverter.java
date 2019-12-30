package com.demo.config.messageconvert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.MappingFastJsonValue;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import org.apache.commons.lang3.CharSet;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义消息转换器
 *
 * @author cc zhao 2019/08/10
 */
public class DemoMessageConverter extends AbstractHttpMessageConverter<Object> {

    public static final MediaType APPLICATION_JAVASCRIPT = new MediaType("application", "javascript");

    /**
     * with fastJson config
     */
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    public DemoMessageConverter() {
        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        // http请求信息处理
        InputStream in = inputMessage.getBody();
        JSON.parseObject(in, Charset.forName("UTF-8"), getType(clazz), Feature.AllowComment);
        return null;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        try {
            HttpHeaders headers = outputMessage.getHeaders();

            //获取全局配置的filter
            SerializeFilter[] globalFilters = fastJsonConfig.getSerializeFilters();
            List<SerializeFilter> allFilters = new ArrayList<SerializeFilter>(Arrays.asList(globalFilters));

            boolean isJsonp = false;

            //不知道为什么会有这行代码， 但是为了保持和原来的行为一致，还是保留下来
            Object value = strangeCodeForJackson(o);

            if (value instanceof FastJsonContainer) {
                FastJsonContainer fastJsonContainer = (FastJsonContainer) value;
                PropertyPreFilters filters = fastJsonContainer.getFilters();
                allFilters.addAll(filters.getFilters());
                value = fastJsonContainer.getValue();
            }

            //revise 2017-10-23 ,
            // 保持原有的MappingFastJsonValue对象的contentType不做修改 保持旧版兼容。
            // 但是新的JSONPObject将返回标准的contentType：application/javascript ，不对是否有function进行判断
            if (value instanceof MappingFastJsonValue) {
                if (!StringUtils.isEmpty(((MappingFastJsonValue) value).getJsonpFunction())) {
                    isJsonp = true;
                }
            } else if (value instanceof JSONPObject) {
                isJsonp = true;
            }


            int len = JSON.writeJSONString(outnew, //
                    fastJsonConfig.getCharset(), //
                    value, //
                    fastJsonConfig.getSerializeConfig(), //
                    //fastJsonConfig.getSerializeFilters(), //
                    allFilters.toArray(new SerializeFilter[allFilters.size()]),
                    fastJsonConfig.getDateFormat(), //
                    JSON.DEFAULT_GENERATE_FEATURE, //
                    fastJsonConfig.getSerializerFeatures());

            if (isJsonp) {
                headers.setContentType(APPLICATION_JAVASCRIPT);
            }

            if (fastJsonConfig.isWriteContentLength()) {
                headers.setContentLength(len);
            }

            outnew.writeTo(outputMessage.getBody());

        } catch (JSONException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        } finally {
            outnew.close();
        }
    }

    private Object strangeCodeForJackson(Object obj) {
        if (obj != null) {
            String className = obj.getClass().getName();
            if ("com.fasterxml.jackson.databind.node.ObjectNode".equals(className)) {
                return obj.toString();
            }
        }
        return obj;
    }

    protected Type getType(Type type) {
        if (Spring4TypeResolvableHelper.isSupport()) {
            Spring4TypeResolvableHelper.getType(type, null);
        }
        return type;
    }

    private static class Spring4TypeResolvableHelper {
        private static boolean hasClazzResolvableType;

        static {
            try {
                Class.forName("org.springframework.core.ResolvableType");
                hasClazzResolvableType = true;
            } catch (ClassNotFoundException e) {
                hasClazzResolvableType = false;
            }
        }

        private static boolean isSupport() {
            return hasClazzResolvableType;
        }


        private static Type getType(Type type, Class<?> contextClass) {
            if (contextClass != null) {
                ResolvableType resolvedType = ResolvableType.forType(type);
                if (type instanceof TypeVariable) {
                    ResolvableType resolvedTypeVariable = resolveVariable((TypeVariable) type, ResolvableType.forClass(contextClass));
                    if (resolvedTypeVariable != ResolvableType.NONE) {
                        return resolvedTypeVariable.resolve();
                    }
                } else if (type instanceof ParameterizedType && resolvedType.hasUnresolvableGenerics()) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Class<?>[] generics = new Class[parameterizedType.getActualTypeArguments().length];
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();

                    for (int i = 0; i < typeArguments.length; ++i) {
                        Type typeArgument = typeArguments[i];
                        if (typeArgument instanceof TypeVariable) {
                            ResolvableType resolvedTypeArgument = resolveVariable((TypeVariable) typeArgument, ResolvableType.forClass(contextClass));
                            if (resolvedTypeArgument != ResolvableType.NONE) {
                                generics[i] = resolvedTypeArgument.resolve();
                            } else {
                                generics[i] = ResolvableType.forType(typeArgument).resolve();
                            }
                        } else {
                            generics[i] = ResolvableType.forType(typeArgument).resolve();
                        }
                    }

                    return ResolvableType.forClassWithGenerics(resolvedType.getRawClass(), generics).getType();
                }
            }

            return type;
        }

        private static ResolvableType resolveVariable(TypeVariable<?> typeVariable, ResolvableType contextType) {
            ResolvableType resolvedType;
            if (contextType.hasGenerics()) {
                resolvedType = ResolvableType.forType(typeVariable, contextType);
                if (resolvedType.resolve() != null) {
                    return resolvedType;
                }
            }

            ResolvableType superType = contextType.getSuperType();
            if (superType != ResolvableType.NONE) {
                resolvedType = resolveVariable(typeVariable, superType);
                if (resolvedType.resolve() != null) {
                    return resolvedType;
                }
            }
            for (ResolvableType ifc : contextType.getInterfaces()) {
                resolvedType = resolveVariable(typeVariable, ifc);
                if (resolvedType.resolve() != null) {
                    return resolvedType;
                }
            }
            return ResolvableType.NONE;
        }
    }
}
