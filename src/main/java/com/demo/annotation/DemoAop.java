package com.demo.annotation;

import java.lang.annotation.*;

/**
 * DESC
 *
 * @author cc.zhao
 * @date 2019/09/10
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DemoAop {

    String value() default "";
}
