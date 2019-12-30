package com.demo.dao;

import com.demo.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * user data access
 *
 * @author cc zhao 2019/07/10
 */
@Mapper
public interface UserDao {

    UserDO getUserById(@Param("uid") Long uid);

    /**
     * 获取所有用户信息
     *
     * @return
     */
    List<UserDO> getAllUser();

    /**
     * 更新用户信息
     *
     * @param tableName
     * @param uid
     * @return
     */
    int updateUserByUid(@Param("tableName") String tableName, @Param("uid") Long uid);

}
