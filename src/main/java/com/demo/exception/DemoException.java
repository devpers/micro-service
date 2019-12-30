package com.demo.exception;

import com.alibaba.dubbo.common.utils.StringUtils;

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
