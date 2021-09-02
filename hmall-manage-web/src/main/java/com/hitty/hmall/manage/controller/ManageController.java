package com.hitty.hmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hitty.hmall.bean.*;
import com.hitty.hmall.service.ManageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ManageController {

    @Reference
    private ManageService manageService;

    /**
     * http://localhost:8082/getCatalog1
     */
    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1(){
        return manageService.getCatalog1();
    }

    /**
     * http://localhost:8082/getCatalog2?catalog1Id=7
     * @return
     */
    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(String catalog1Id){
        return manageService.getCatalog2(catalog1Id);
    }

    /**
     * http://localhost:8082/getCatalog3?catalog2Id=13
     */
    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        return manageService.getCatalog3(catalog2Id);
    }

    /**
     * http://localhost:8082/attrInfoList?catalog3Id=61
     */
    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(String catalog3Id){
        return manageService.getAttrInfoList(catalog3Id);
    }

    /**
     * http://localhost:8082/saveAttrInfo
     */
    @RequestMapping("saveAttrInfo")
    public void saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        manageService.saveAttrInfo(baseAttrInfo);
    }

    /**
     * http://localhost:8082/getAttrValueList?attrId=106
     */
//    @RequestMapping("getAttrValueList")
//    public List<BaseAttrValue> getAttrValueList(String attrId){
//        return manageService.getAttrValueList(attrId);
//    }

    @RequestMapping("getAttrValueList")
    public List<BaseAttrValue> getAttrValueList(String attrId){
        BaseAttrInfo baseAttrInfo = manageService.getAttrInfo(attrId);
        return baseAttrInfo.getAttrValueList();
    }


    /**
     * http://localhost:8082/baseSaleAttrList
     */
    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr> getBaseSaleAttrList(){
        return manageService.getBaseSaleAttrList();
    }


    /**
     * http://localhost:8082/saveSpuInfo
     */
    @RequestMapping("saveSpuInfo")
    public void saveSpuInfo(@RequestBody SpuInfo spuInfo){
        if (spuInfo != null){
            manageService.saveSpuInfo(spuInfo);
        }
    }
}
