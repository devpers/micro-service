package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.util.Assert;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * demo service filter array
 *
 * @author cc.zhao
 * @date 2019/08/31
 */
public interface DemoFilter {


    /**
     * even number
     *
     * @return
     */
    Predicate<Integer> isEvenNumber();

    /**
     * odd number
     *
     * @return
     */
    Predicate<Integer> isOddNumber();

    /**
     * grater than num
     *
     * @return
     */
    Predicate<Integer> isGraterThanNumber(Integer num);

    /**
     * less than number
     *
     * @param num
     * @return
     */
    Predicate<Integer> isLessThanNumber(Integer num);

    /**
     * execute filters
     */
    default void executeFilters(List<Predicate<Integer>> filters, List<Integer> numbers) {
        if (Assert.isEmpty(filters)) {
            System.out.println("filter list empty");
        }

        Iterator<Integer> integerIterator = numbers.iterator();
        while (integerIterator.hasNext()) {
            Integer num = integerIterator.next();
            for (Predicate<Integer> filter : filters) {
                if (!filter.test(num)) {
                    integerIterator.remove();
                }
            }
        }

        numbers = Lists.newArrayList(integerIterator);
        System.out.println(JSONObject.toJSONString(numbers));
    }

}