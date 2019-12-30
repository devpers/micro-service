package com.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * data source configuration
 *
 * @author cc zhao 2019/07/10
 */
@Configuration
public class DataSourceConfig {

    /**
     * Autodetect an existing DataSource.
     * Will create and register an instance of a SqlSessionFactory passing that DataSource as an input using the SqlSessionFactoryBean.
     * Will create and register an instance of a SqlSessionTemplate got out of the SqlSessionFactory.
     * Autoscan your mappers, link them to the SqlSessionTemplate and register them to Spring context so they can be injected into your beans.
     *
     * @return
     */
    @Bean("dataSource")
    @ConfigurationProperties(prefix = "demo.db")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

}
