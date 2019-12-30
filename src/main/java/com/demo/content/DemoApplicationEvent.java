package com.demo.content;

import java.util.EventObject;

/**
 * demo service 的公共事件。
 *
 * <p>
 * 所有发布的事件应该继承该类，可自定义每个事件内部的内容
 * </p>
 *
 * @author cc zhao 2019-09-21
 */
public class DemoApplicationEvent extends EventObject {

    private long timeStamp = 0L;

    public DemoApplicationEvent(Object source) {
        super(source);
        this.timeStamp = System.currentTimeMillis();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
