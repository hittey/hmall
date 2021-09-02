package com.hitty.hmall.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuLsResult implements Serializable {

     List<SkuLsInfo> skuLsInfoList;
     List<String> valueIdList;
     long total;
     long totalPages;
}
