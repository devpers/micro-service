package com.micro.exception;

/**
 * 异常基类
 *
 * @author cc zhao 2019/08/10
 */
public class DemoException extends RuntimeException {

    public DemoException(String message) {
        super(message);
    }

}
