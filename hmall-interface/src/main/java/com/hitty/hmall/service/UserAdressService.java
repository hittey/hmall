package com.hitty.hmall.service;

import com.hitty.hmall.bean.UserAddress;

import java.util.List;

public interface UserAdressService {
    List<UserAddress> getUserAdressList(String userId);
}
