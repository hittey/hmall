package com.hitty.hmall.user.controller;

import com.hitty.hmall.bean.UserInfo;
import com.hitty.hmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("findAll")
    public List<UserInfo> findAll(){
        return userService.findAll();
    }

}
