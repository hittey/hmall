package com.hitty.hmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hitty.hmall.bean.UserAddress;
import com.hitty.hmall.service.UserAdressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {
    @Reference
    private UserAdressService userAdressService;
    @RequestMapping("trade")
    @ResponseBody
    public List<UserAddress> trade(String userId){
        return userAdressService.getUserAdressList(userId);
    }
}
