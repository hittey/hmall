package com.hitty.hmall.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuInfo implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String spuId;
    @Column
    private String price;
    @Column
    private String skuName;
    @Column
    private String skuDesc;
    @Column
    private BigDecimal weight;
    @Column
    private String catalog3Id;
    @Column
    private String skuDefaultImg;
    @Transient
    List<SkuImage> skuImageList;
    @Transient
    List<SkuAttrValue> skuAttrValueList;
    @Transient
    List<SkuSaleAttrValue> skuSaleAttrValueList;



}
