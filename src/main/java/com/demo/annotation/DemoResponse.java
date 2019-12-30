package com.demo.annotation;

import java.lang.annotation.*;

/**
 * 自定义http返回值处理
 *
 * @author cc zhao
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DemoResponse {

    String value() default "";
}
