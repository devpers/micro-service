package com.demo.service.impl;

import static com.alibaba.fastjson.JSON.*;

import com.alibaba.fastjson.JSON;
import com.demo.dao.UserDao;
import com.demo.entity.UserDO;
import com.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用户服务
 *
 * @author cc zhao 2019/07/10
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDO getUserById(Long uid) {
        UserDO user = userDao.getUserById(uid);
        switch (uid.intValue()) {
            case 1:
                log.info(user.getName());
            case 2:
                log.info(toJSONString(user));
            default:
                log.info("no user info");
        }
        return user;
    }

    @Override
    public List<UserDO> getAllUserDO() {
        return userDao.getAllUser();
    }

    @Override
    public String sayHello2User(String userName) {
        return "hello" + userName;
    }


}
