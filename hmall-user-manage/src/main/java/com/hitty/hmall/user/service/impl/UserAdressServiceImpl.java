package com.hitty.hmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hitty.hmall.bean.UserAddress;
import com.hitty.hmall.service.UserAdressService;
import com.hitty.hmall.user.mapper.UserAdressMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UserAdressServiceImpl implements UserAdressService {
    @Autowired
    private UserAdressMapper userAdressMapper;

    @Override
    public List<UserAddress> getUserAdressList(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(userId);
        return userAdressMapper.select(userAddress);
    }
}
