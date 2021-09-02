package com.hitty.hmall.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
public class SpuInfo implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String spuName;
    @Column
    private String description;
    @Column
    private String catalog3Id;
    @Transient
    private List<SpuImage> spuImageList;
    @Transient
    private List<SpuSaleAttr> spuSaleAttrList;

}
