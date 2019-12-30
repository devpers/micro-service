package com.demo.config;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DESC
 *
 * @author cc zhao 2019/08/12
 */
public class DemoBaseConfig {

    public static void main(String[] args) throws InterruptedException {
//        Thread.sleep(1000, 0);
//        demoHook();
        initFlowRules();
        while (true) {
            try (Entry entry = SphU.entry("HelloWorld")) {
                System.out.println("resource protected by sentinel");
            } catch (BlockException e) {
                System.out.println("blocked!");
            }
        }
    }

    public static void demoHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("do something after jvm shutdown")));
    }

    private static void initFlowRules() {
        FlowRule flowRule = new FlowRule();
        flowRule.setRefResource("HelloWorld");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(20);
        FlowRuleManager.loadRules(Arrays.asList(flowRule));
    }

}
