package com.hitty.hmall.service;

import com.hitty.hmall.bean.SkuLsInfo;
import com.hitty.hmall.bean.SkuLsParam;
import com.hitty.hmall.bean.SkuLsResult;

public interface ListService {

    void saveSkuInfo(SkuLsInfo skuLsInfo);

    SkuLsResult search(SkuLsParam skuLsParam);

    void incrHotScore(String skuId);
}
