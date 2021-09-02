package com.hitty.hmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hitty.hmall.bean.UserInfo;
import com.hitty.hmall.config.RedisUtil;
import com.hitty.hmall.service.UserService;
import com.hitty.hmall.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public String userKey_prefix = "user:";
    public String userInfoKey_suffix = ":info";
    public int userKey_timeout = 24*60*60;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        String Pwd = userInfo.getPasswd();
        String newPwd = DigestUtils.md5DigestAsHex(Pwd.getBytes());
        userInfo.setPasswd(newPwd);
        UserInfo info = userInfoMapper.selectOne(userInfo);
        if (info !=null){
            Jedis jedis = redisUtil.getJedis();
            String userKey = userKey_prefix+userInfo.getId()+userInfoKey_suffix;
            jedis.setex(userKey,userKey_timeout, JSON.toJSONString(info));
            jedis.close();
            return info;
        }
        return null;
    }

    @Override
    public UserInfo verify(String userId) {
        String userKey = userKey_prefix+userId+userInfoKey_suffix;
        Jedis jedis = redisUtil.getJedis();
        String userJson = jedis.get(userKey);
        if(!StringUtils.isEmpty(userJson)){
            UserInfo userInfo = JSON.parseObject(userJson, UserInfo.class);
            if(jedis != null){
                   jedis.close();
              }
            return userInfo;
        }
        return null;
    }
}
