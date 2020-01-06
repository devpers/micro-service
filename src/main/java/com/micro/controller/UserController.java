package com.micro.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSONObject;
import com.micro.annotation.DemoAop;
import com.micro.annotation.DemoResponse;
import com.micro.entity.UserDO;
import com.micro.service.DemoFilter;
import com.micro.service.UserService;
import com.micro.vo.UserQueryVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 用户服务
 *
 * @author cc zhao 2019/07/10
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    DemoFilter demoFilter;

    @GetMapping("/getUserInfo")
    @ResponseStatus(value = HttpStatus.OK)
    public void gerAllUserInfo() {
        System.out.println(JSONObject.toJSONString(userService.getAllUserDO()));
    }

    @GetMapping("/hello/{dubbo}")
    @ResponseStatus(value = HttpStatus.OK)
    public void sayHello2User(@PathVariable("dubbo") String dubbo) {
        System.out.println(dubbo);
    }

    @DemoResponse
    @DemoAop
    @RequestMapping(value = "/getUser", method = {RequestMethod.GET, RequestMethod.POST})
    public UserDO getUser(UserQueryVO userQueryVO) {
        try {
            logger.info(JSONObject.toJSONString(userQueryVO));

            UserDO user = userService.getUserById(userQueryVO.getUid());

            UserDO userDO = new UserDO();
            userDO.setUid(userQueryVO.getUid());
            userDO.setName(userQueryVO.getName());
            userDO.setAddTime(LocalDateTime.now());

            return user;
        } catch (Exception e) {
            log.info("异常测试", e);
        }
        return null;
    }

    @GetMapping("/executeFilter")
    @DemoResponse
    public void demoFilter() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 0);

        List<Predicate<Integer>> filters = Lists.newArrayList();

        // even number
        filters.add(demoFilter.isEvenNumber());

        // num > 0
        filters.add(demoFilter.isGraterThanNumber(0));

        // num < 9
        filters.add(demoFilter.isLessThanNumber(9));
    }

    @GetMapping("/sentinel")
    public void sentinel() {
        initFlowRules();
        while (true) {
            try (Entry entry = SphU.entry("HelloWorld")) {
                System.out.println("resource protected by sentinel");
            } catch (BlockException e) {
                System.out.println("blocked!");
            }
        }
    }

    private static void initFlowRules() {
        FlowRule flowRule = new FlowRule();
        flowRule.setRefResource("HelloWorld");
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setCount(20);
        FlowRuleManager.loadRules(Arrays.asList(flowRule));
    }

}
