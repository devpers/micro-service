package com.micro;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * demo-ms-service入口（注意该类的位置）
 *
 * @author cc zhao 2019/07/10
 */
@SpringBootApplication
@ServletComponentScan("com.demo.config")
@Slf4j
public class MicroApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MicroApplication.class);
        applicationContext.publishEvent(new DemoEvent("micro-service start"));
    }

    static class DemoEvent extends ApplicationEvent {
        /**
         * Create a new ApplicationEvent.
         *
         * @param source the object on which the event initially occurred (never {@code null})
         */
        public DemoEvent(Object source) {
            super(source);
            log.info(JSONObject.toJSONString(source));
        }
    }

}
