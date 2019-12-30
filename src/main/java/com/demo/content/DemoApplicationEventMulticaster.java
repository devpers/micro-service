package com.demo.content;

import java.util.Arrays;
import java.util.List;

/**
 * 广播事件向监听器
 *
 * @author cc zhao 2019-09-21
 */
public class DemoApplicationEventMulticaster {

    private List<DemoEventListener> eventListeners;

    private DemoApplicationEvent demoApplicationEvent;

    public DemoApplicationEventMulticaster(List<DemoEventListener> eventListeners, DemoApplicationEvent demoApplicationEvent) {
        this.eventListeners = eventListeners;
        this.demoApplicationEvent = demoApplicationEvent;
    }

    private void doMulticaster() {
        // 向监听者广播事件
        for (DemoEventListener listener : eventListeners){
            listener.onApplicationEventHandle(demoApplicationEvent);
        }
    }

    public static void main(String[] args) {
        DemoApplicationEvent event = new DemoApplicationEvent("事件模型");

        DemoApplicationEventMulticaster multicaster = new DemoApplicationEventMulticaster(Arrays.asList(new DemoApplicationEventListener()), event);

        multicaster.doMulticaster();
    }
}
