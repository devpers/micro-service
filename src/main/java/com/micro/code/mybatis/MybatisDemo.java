package com.micro.code.mybatis;

import static com.alibaba.fastjson.JSON.*;

import com.micro.dao.UserDao;
import com.micro.entity.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * desc
 *
 * @author cc zhao 2019-11-30
 */
@Configuration
@Slf4j
public class MybatisDemo {

    /**
     * xml配置获取sqlSessionFactory
     *
     * @return
     * @throws IOException
     */
    @Bean
    public SqlSessionFactory initDemoSqlSessionFactory() {
        return getSqlSessionFactory();
    }

    /**
     * 自定以sqlSessionFactory。使用单例模式
     *
     * @return
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        try {
            return new SqlSessionFactoryBuilder().build(new ClassPathResource("mybatis-config.xml").getInputStream());
        } catch (Exception e) {
            log.error("初始化sqlSessionFactory异常", e);
        }
        return null;
    }

    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        UserDO user = userDao.getUserById(1L);
        System.out.println(toJSONString(user));

        UserDO result = sqlSession.selectOne("getUserById", 1);
        System.out.println(result);
    }
}