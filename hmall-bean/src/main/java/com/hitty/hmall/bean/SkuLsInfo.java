package com.hitty.hmall.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuLsInfo implements Serializable {

    private String id;
    private String catalog3Id;
    private List<SkuLsAttrValue> skuAttrValueList;
    private String skuName;
    private String price;
    private String skuDefaultImg;
    private Long hotScore=0L;

}
