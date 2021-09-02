package com.hitty.hmall.service;

import com.hitty.hmall.bean.UserInfo;

import java.util.List;

public interface UserService {
    List<UserInfo> findAll();

    UserInfo login(UserInfo userInfo);

    UserInfo verify(String userId);
}
