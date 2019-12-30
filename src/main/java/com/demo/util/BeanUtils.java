package com.demo.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * bean 工具
 *
 * @author cc zhao
 * @date 2019/12/02
 */
public class BeanUtils {


    /**
     * 对象强检查是否为空，每个属性都不允许为空。只是针对getter方法，有局限性，谨慎使用
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> boolean attrStrongCheckOk(T object) {
        if (Assert.isEmpty(object)) {
            return false;
        }

        Class clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        if (Assert.isEmpty(methods)) {
            return true;
        }

        Method emptyMethod = new ArrayList<>(Arrays.asList(methods)).stream().filter(m -> m.getName().startsWith("get")).filter(m -> {
            try {
                return Assert.isEmpty(m.invoke(object));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return true;
            }
        }).findAny().orElse(null);

        return Assert.isEmpty(emptyMethod);
    }

}
