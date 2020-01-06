package com.micro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.micro.service.DemoFilter;
import com.micro.util.Assert;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * demo service number filter
 *
 * @author cc.zhao
 * @date 2019/08/31
 */
@Service("demoFilterImpl")
public class DemoFilterImpl implements DemoFilter {

    @Override
    public Predicate<Integer> isEvenNumber() {
        // % 判断奇偶数
        return num -> num % 2 == 0;
    }

    @Override
    public Predicate<Integer> isOddNumber() {
        return num -> num % 2 != 0;
    }

    @Override
    public Predicate<Integer> isGraterThanNumber(Integer number) {
        return num -> num > number;
    }

    @Override
    public Predicate<Integer> isLessThanNumber(Integer number) {
        return num -> num < number;
    }

    public static void main(String[] args) {
        DemoFilterImpl demoFilter = new DemoFilterImpl();
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);
        List<Predicate<Integer>> filters = Arrays.asList(
                demoFilter.isEvenNumber(), demoFilter.isGraterThanNumber(3), demoFilter.isLessThanNumber(8));
        demoFilter.executeFilter(filters, numbers);
    }

    private void executeFilter(List<Predicate<Integer>> filters, List<Integer> numbers) {
        if (Assert.isEmpty(filters)) {
            System.out.println("filter list empty");
        }

        Set<Integer> illegal = Sets.newHashSet();
        for (Predicate<Integer> filter : filters) {
            // 满足当前过滤条件的进入下轮过滤，直至走完全部过滤器
            for (Integer num : numbers) {
                if (!filter.test(num)) {
                    illegal.add(num);
                }
            }
            numbers = numbers.stream().filter(n -> !illegal.contains(n)).collect(Collectors.toList());
        }

        System.out.println("不符合条件元素=" + JSONObject.toJSONString(illegal));
        numbers = numbers.stream().filter(n -> !illegal.contains(n)).collect(Collectors.toList());
        System.out.println(JSONObject.toJSONString(numbers));
    }
}
