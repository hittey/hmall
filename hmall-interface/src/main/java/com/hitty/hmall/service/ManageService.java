package com.hitty.hmall.service;

import com.hitty.hmall.bean.*;

import java.util.List;

public interface ManageService {
    /**
     * 获取所有一级分类数据
     * @return
     */
    List<BaseCatalog1> getCatalog1();

    /**
     * 根据一级分类id获取所有二级分类数据
     * @param catalog1Id
     * @return
     */
    List<BaseCatalog2> getCatalog2(String catalog1Id);

    /**
     * 根据二级分类id获取所有三级分类数据
     * @param catalog2Id
     * @return
     */
    List<BaseCatalog3> getCatalog3(String catalog2Id);

    /**
     * 根据三级分类id获取平台属性
     * @param catalog3Id
     * @return
     */
    List<BaseAttrInfo> getAttrInfoList(String catalog3Id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    List<BaseAttrValue> getAttrValueList(String attrId);

    BaseAttrInfo getAttrInfo(String attrId);


    List<BaseSaleAttr> getBaseSaleAttrList();

    void saveSpuInfo(SpuInfo spuInfo);

    List<SpuImage> getSpuImageList(SpuImage spuImage);

    List<SpuSaleAttr> getSpuSaleAttrList(String spuId);

    void saveSkuInfo(SkuInfo skuInfo);

    SkuInfo getSkuInfo(String skuId);

    List<SkuImage> getSkuImage(String skuId);

    List<SpuSaleAttr> getSpuSaleAttrListBySku(SkuInfo skuInfo);

    List<SkuSaleAttrValue> getSkuSaleAttrValueLisrBySku(SkuInfo skuInfo);

    List<BaseAttrInfo> getAttrInfoList(List<String> valueIdList);
}
