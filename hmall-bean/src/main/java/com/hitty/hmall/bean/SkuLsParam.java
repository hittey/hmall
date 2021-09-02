package com.hitty.hmall.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuLsParam implements Serializable {

     String catalog3Id;
     String keyword;
     String[] valueId;
     int pageNo=1;
     int pageSize=20;
}
