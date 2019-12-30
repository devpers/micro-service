package com.demo.util;


import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * 类加载工具
 *
 * @author cc.zhao
 * @date 2019/09/19
 */
public class ClassUtils {

    public static void main(String[] args) {
        loadSameClass();
    }

    public static void loadSameClass() {
        try {
            // TODO
            String classPath = "com.demo.sdk.DemoSdk";

            Class clazz1 = Class.forName(classPath, true,
                    new URLClassLoader(new URL[]{new File("./src/main/resources/sdk1/demoSdk-1.0-SNAPSHOT.jar").toURI().toURL()}));
            Object invokeObj1 = clazz1.getDeclaredMethod("sayHello").invoke(clazz1.newInstance(), (Object[]) null);

            Class clazz2 = Class.forName(classPath, true,
                    new URLClassLoader(new URL[]{new File("./src/main/resources/sdk2/demoSdk-1.0-SNAPSHOT.jar").toURI().toURL()}));
            Object invokeObj2 = clazz2.getDeclaredMethod("sayHello").invoke(clazz2.newInstance(), (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
