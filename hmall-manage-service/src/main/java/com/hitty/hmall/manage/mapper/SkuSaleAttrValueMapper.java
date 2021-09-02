package com.hitty.hmall.manage.mapper;

import com.hitty.hmall.bean.SkuSaleAttrValue;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {

    /**
     * 根据spuId获取skuSaleAttrValueList
     * @param spuId
     * @return
     */
    List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySku(String spuId);
}
