package com.micro.dao;

import com.micro.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * user data access
 *
 * @author cc zhao 2019/07/10
 */
@Mapper
public interface UserDao {

    /*
     * xml配置mapper查询
     */
    UserDO getUserDO();

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


    /*
     * 注解查询
     */
//    @Select("select * from user where uid = #{uid}")
//    UserDO getUser(@Param("uid") Long uid);


}
