package com.hitty.hmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.hitty.hmall.bean.SkuInfo;
import com.hitty.hmall.bean.SkuSaleAttrValue;
import com.hitty.hmall.bean.SpuSaleAttr;
import com.hitty.hmall.service.ListService;
import com.hitty.hmall.service.ManageService;
import net.minidev.json.JSONUtil;
import org.json.JSONString;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItemController {

    @Reference
    private ManageService manageService;

    @Reference
    private ListService listService;

    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, HttpServletRequest request){
        //根据skuId回显商品详情和图片信息
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);

        //回显销售属性，销售属性值
        List<SpuSaleAttr> spuSaleAttrList = manageService.getSpuSaleAttrListBySku(skuInfo);

        //切换商品
        List<SkuSaleAttrValue> skuSaleAttrValueList = manageService.getSkuSaleAttrValueLisrBySku(skuInfo);
        String key = "";
        HashMap<String, String> skuSpuHashMap = new HashMap<>();
        for (int i = 0; i < skuSaleAttrValueList.size(); i++) {
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueList.get(i);
            if(key.length()>0){
                key+="|";
            }
            key+=skuSaleAttrValue.getSaleAttrValueId();
             if(i==skuSaleAttrValueList.size()-1|| !skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueList.get(i+1).getSkuId())){
                 skuSpuHashMap.put(key,skuSaleAttrValue.getSkuId());
                 key="";
             }
        }
        String skuJson= JSON.toJSONString(skuSpuHashMap);

        request.setAttribute("spuSaleAttrList",spuSaleAttrList);
        request.setAttribute("valuesSkuJson",skuJson);
        request.setAttribute("skuInfo",skuInfo);
        listService.incrHotScore(skuId);
        return "item";
    }
}
