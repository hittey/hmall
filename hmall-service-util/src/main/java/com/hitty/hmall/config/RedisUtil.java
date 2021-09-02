package com.hitty.hmall.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private JedisPool jedisPool;

    public void initJedisPool(String host,int port,int database){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //设置最大连接数
        jedisPoolConfig.setMaxTotal(200);
        //设置最大延迟时间
        jedisPoolConfig.setMaxWaitMillis(10*1000);
        //设置最小在用连接数
        jedisPoolConfig.setMinIdle(10);
        //设置连接可用测试
        jedisPoolConfig.setTestOnBorrow(true);
        //设置最大连接数时等待
        jedisPoolConfig.setBlockWhenExhausted(true);
        jedisPool = new JedisPool(jedisPoolConfig,host,port,20*1000);
    }

    public Jedis getJedis(){

        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
