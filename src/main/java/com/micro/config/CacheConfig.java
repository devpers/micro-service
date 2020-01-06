package com.micro.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 缓存配置（Redisson/Jedis/guava/caffeine）
 * 配置服务中的缓存体系（如：一/二级缓存等），自定义缓存策略（时间、粒度）
 * 同时需要考虑缓存中经常出现问题，如：线程安全、缓存失效、雪崩、穿透
 *
 * <p>
 * 缓存存取注意序列化和反序列化
 * </p>
 *
 * @author cc.zhao
 * @date 2019/08/12
 */
@Configuration
public class CacheConfig {

    @Value("127.0.0.1")
    private String redisHost;

    @Value("6379")
    private String redisPort;

    @Value("000000")
    private String redisPassword;

    private static Cache<String, String> cache = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build();

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort).setPassword(redisPassword);
        return Redisson.create(config);
    }

    /**
     * 使用单例模式提供redis
     *
     * @return
     */
    public static Jedis initJedis() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("000000");
        return jedis;
    }

    public static void main(String[] args) {
        cache.put("cache", "caffeine");
        System.out.println(cache.get("cache", Function.identity()));
        System.out.println(cache.getIfPresent("cache"));

        Jedis jedis = initJedis();

        long now = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            String temp = String.valueOf(i);
            jedis.setnx(temp, temp + "-jedis-keys");
            System.out.println(temp);
        }
        long time = System.currentTimeMillis() - now;
        System.out.println("时间=" + time);


        System.out.println("测试判断使用是否有某一key");
    }


}
