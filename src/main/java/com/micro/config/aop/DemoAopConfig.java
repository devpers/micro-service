package com.micro.config.aop;

import com.alibaba.fastjson.JSONObject;
import com.micro.annotation.DemoAop;
import com.micro.entity.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * config demo service aspect
 *
 * @author cc.zhao
 * @date 2019/09/09
 */
@Aspect
@Component
@Slf4j
@Order(1)
public class DemoAopConfig implements Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * config pointcut
     * all method will be ... under <p>com.demo.controller</p> package
     * within(com.demo.controller.*)
     */
    @Pointcut("@annotation(com.micro.annotation.DemoAop)")
    public void httpServletLog() {

    }

    /**
     * ProceedingJoinPoint is only supported for around advice
     */
    @Around("com.micro.config.aop.DemoAopConfig.httpServletLog()")
    public Object aroundOperation(ProceedingJoinPoint point) throws Throwable {
        try {
            Signature signature = point.getSignature();
            log.info(JSONObject.toJSONString(signature));
            Class clazz = signature.getDeclaringType();
            String typeName = signature.getDeclaringTypeName();

            // query argument
            Object[] args = point.getArgs();
            Object objects = args[0];

            Class userControllerClass = Class.forName(signature.getDeclaringTypeName());
            // the method of request
            // do something
            Object returnObject = point.proceed();

            // if the method have <code>DemoAop.class</code> annotation, we can process the return value before the return operation
            Method[] methods = userControllerClass.getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(DemoAop.class)) {
                    log.info(m.getName());
                    // if more method have the annotation that we want, we need judge difference method
                    UserDO userDO = (UserDO) returnObject;
                    // change return value
                    userDO.setName(userDO.getName() + ", come on");
                    userDO.setAddTime(LocalDateTime.of(2015, 9, 10, 17, 25));
                }
            }

            return returnObject;
        } catch (Exception e) {
            log.error("around advice");
        }
        return null;
    }

    @Before("com.micro.config.aop.DemoAopConfig.httpServletLog()")
    public void beforeOperationLog(JoinPoint point) {
        point.getSignature();
        log.info("before advice");
    }

    @After("com.micro.config.aop.DemoAopConfig.httpServletLog()")
    public void afterOperationLog(JoinPoint point) {
        log.info("after advice");
    }

}
