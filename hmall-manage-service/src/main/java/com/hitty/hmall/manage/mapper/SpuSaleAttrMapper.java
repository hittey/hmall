package com.hitty.hmall.manage.mapper;

import com.hitty.hmall.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    List<SpuSaleAttr> selectSpuSaleAttrList(String spuId);

    /**
     * 根据spuId获取销售属性，销售属性值
     * 根据skuId锁定销售属性值
     * @param id
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> selectSpuSaleAttrListBySku(String id, String spuId);
}
