package com.hitty.hmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hitty.hmall.bean.SkuInfo;
import com.hitty.hmall.bean.SkuLsInfo;
import com.hitty.hmall.bean.SpuImage;
import com.hitty.hmall.bean.SpuSaleAttr;
import com.hitty.hmall.service.ListService;
import com.hitty.hmall.service.ManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class SkuManageController {

    @Reference
    private ManageService manageService;

    @Reference
    private ListService listService;

    /**
     * http://localhost:8082/spuSaleAttrList?spuId=58
     * 回显销售属性和销售属性值
     * @param spuId
     * @return
     */
    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId){
        return manageService.getSpuSaleAttrList(spuId);
    }

    /**
     * http://localhost:8082/spuImageList?spuId=58
     * 回显spu图片
     * @param spuImage
     * @return
     */
    @RequestMapping("spuImageList")
    public List<SpuImage> getSpuImageList(SpuImage spuImage){
        return manageService.getSpuImageList(spuImage);
    }

    /**
     * http://localhost:8082/saveSkuInfo
     * 添加
     */
    @RequestMapping("saveSkuInfo")
    public void saveSkuInfo(@RequestBody SkuInfo skuInfo){
        manageService.saveSkuInfo(skuInfo);
    }


    @RequestMapping("onSale")
    public void onSave(String skuId){
        SkuInfo skuInfo = manageService.getSkuInfo(skuId);
        SkuLsInfo skuLsInfo = new SkuLsInfo();
        BeanUtils.copyProperties(skuInfo,skuLsInfo);
        listService.saveSkuInfo(skuLsInfo);

    }
}
