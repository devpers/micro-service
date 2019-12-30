package com.demo.content;

import java.util.EventListener;

/**
 * 公共的事件监听接口，实现该接口，自定义方法onApplicationEventHandle实现处理事件
 *
 * @author cc zhao
 * @date 2019-09-21
 */
@FunctionalInterface
public interface DemoEventListener<T extends DemoApplicationEvent> extends EventListener {

    /**
     * 处理监听事件
     *
     * @param event
     */
    void onApplicationEventHandle(T event);

}
