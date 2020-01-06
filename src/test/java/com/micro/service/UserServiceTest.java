package com.micro.service;

import com.micro.dao.UserDao;
import com.micro.entity.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * junit test
 *
 * @author cc.zhao
 * @date 2019/08/31
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    UserDao userDao;

    @Test
    public void getUser() {
        UserDO userById = userDao.getUserById(1L);
        System.out.println(userById);
    }
}
