package com.hitty.hmall.passport;

import com.hitty.hmall.passport.config.JwtUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HmallPassportWebApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testJwt(){
        String key = "hitty";
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId","1001");
        map.put("nickName","admin");
        String salt = "192.168.43.129";
        String tocken = JwtUtil.encode(key, map, salt);

        System.out.println("tocken+"+tocken);

        Map<String, Object> maps = JwtUtil.decode(tocken, key, salt);
        System.out.println(maps);
    }

}
