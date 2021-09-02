package com.hitty.hmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hitty.hmall.bean.SpuInfo;
import com.hitty.hmall.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class SpuInfoController {

    @Reference
    private SpuInfoService spuInfoService;
    /**
     * http://localhost:8082/spuList?catalog3Id=61
     */
    @RequestMapping("spuList")
    public List<SpuInfo> getSpuList(String catalog3Id){
        return spuInfoService.getSpuList(catalog3Id);
    }
}
