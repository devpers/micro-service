package com.demo.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * collection util
 *
 * @author cc.zhao
 * @date 2019/09/12
 */
public class CollectionUtils {

    /**
     * 交集
     *
     * @param c1
     * @param c2
     * @return
     */
    public static <T> List<T> intersection(List<T> c1, List<T> c2) {
        if (Assert.isEmpty(c1) || Assert.isEmpty(c2)) {
            return null;
        }
        return c1.stream().filter(c -> c2.contains(c)).collect(Collectors.toList());
    }

    /**
     * difference of between c1 and c2
     *
     * @param c1
     * @param c2
     * @param <T>
     * @return
     */
    public static <T> Collection<T> difference(Collection<T> c1, Collection<T> c2) {
        Collection<T> dc1 = differenceC1ToC2(c1, c2);
        Collection<T> dc2 = differenceC2ToC1(c1, c2);
        if (Assert.isEmpty(dc1)) {
            return dc2;
        }

        if (Assert.isEmpty(dc2)) {
            return dc1;
        }

        dc2.addAll(dc1);
        return dc2;
    }

    /**
     * 差集 c1 > c2
     *
     * @param c1
     * @param c2
     * @return
     */
    public static <T> Collection<T> differenceC1ToC2(Collection<T> c1, Collection<T> c2) {
        if (Assert.isEmpty(c1)) {
            return null;
        }

        if (Assert.isEmpty(c2)) {
            return c1;
        }

        return c1.stream().filter(c -> !c2.contains(c)).collect(Collectors.toList());
    }

    /**
     * 差集 c2 > c1
     *
     * @param c1
     * @param c2
     * @return
     */
    public static <T> Collection<T> differenceC2ToC1(Collection<T> c1, Collection<T> c2) {
        if (Assert.isEmpty(c1)) {
            return c2;
        }

        if (Assert.isEmpty(c2)) {
            return null;
        }

        return c2.stream().filter(c -> !c1.contains(c)).collect(Collectors.toList());
    }



}
