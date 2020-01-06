package com.micro.content;

import com.alibaba.fastjson.JSONObject;

/**
 * 实现事件监听接口<code>DemoEventListener</code>
 * <p>
 * 在方法<method>onApplicationEventHandle</method>中实现具体的事件处理逻辑
 * </p>
 *
 * @author cc zhao 2019-09-21
 */
public class DemoApplicationEventListener<T extends DemoApplicationEvent> implements DemoEventListener<T> {

    @Override
    public void onApplicationEventHandle(T event) {
        System.out.println(JSONObject.toJSONString(event.getSource()));
    }
}
