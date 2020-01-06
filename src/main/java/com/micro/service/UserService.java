package com.micro.service;

import com.micro.entity.UserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * user service
 *
 * @author cc zhao 2019/07/10
 */
public interface UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    default void user() {
        logger.info("user service");
    }

    UserDO getUserById(Long uid);

    /**
     * 获取用户信息
     *
     * @return
     */
    List<UserDO> getAllUserDO();

    /**
     * 用户hello
     *
     * @param userName
     * @return
     */
    String sayHello2User(String userName);
}
