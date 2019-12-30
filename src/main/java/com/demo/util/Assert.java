package com.demo.util;

import com.demo.exception.DemoException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 断言
 *
 * @author cc zhao 2019/08/10
 */
public class Assert {

    /**
     * 断言是否为空
     *
     * @param o
     * @param message
     */
    public static void notNull(Object o, String message) {
        if (isEmpty(o)) {
            throw new DemoException(message);
        }
    }

    /**
     * 断言是否为假
     *
     * @param flag
     * @param message
     */
    public static void isTrue(boolean flag, String message) {
        if (!flag) {
            throw new DemoException(message);
        }
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 检查是否为空，null
     * 支持基本对象，list，set，map，数组
     *
     * @param o
     * @return
     */
    public static boolean isEmpty(Object o) {
        if (null == o || "".equals(o)) {
            return true;
        } else if (o instanceof String) {
            return StringUtils.isBlank(((String) o).trim());
        } else if (o instanceof Collection) {
            return ((Collection) o).isEmpty();
        } else if (o.getClass().isArray()) {
            return Array.getLength(o) == 0;
        } else if (o instanceof Map) {
            return ((Map) o).isEmpty();
        }
        return false;
    }
}
