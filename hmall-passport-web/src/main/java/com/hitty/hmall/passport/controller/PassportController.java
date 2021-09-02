package com.hitty.hmall.passport.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.hitty.hmall.bean.UserInfo;
import com.hitty.hmall.passport.config.JwtUtil;
import com.hitty.hmall.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PassportController {

    @Value("${tocken.key}")
    private String key;

    @Reference
    private UserService userService;

    @RequestMapping("index")
    public String index(HttpServletRequest request){
        String originUrl = request.getParameter("originUrl");
        request.setAttribute("originUrl",originUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(UserInfo userInfo, HttpServletRequest request){
        String salt = request.getHeader("X-forwarded-for");
        UserInfo info = userService.login(userInfo);
        if (info !=null){

            HashMap<String, Object> map = new HashMap<>();
            map.put("userId",userInfo.getId());
            map.put("nickName",userInfo.getNickName());
            String tocken = JwtUtil.encode(key,map,salt);

            return tocken;
        } else {
            return "fail";
        }
    }

    @RequestMapping("verify")
    @ResponseBody
    public String verify(HttpServletRequest request){
        String tocken = request.getParameter("newTocken");
        String salt = request.getHeader("X-forwarded-for");

        Map<String, Object> map = JwtUtil.decode(tocken, key, salt);
        if( map!= null && map.size()>0 ){
            String userId = (String) map.get("userId");
            UserInfo userInfo = userService.verify(userId);
            if(userInfo !=null){
                return "success";
            } else {
                return "fail";
            }
          }
        return "fail";
    }
}
