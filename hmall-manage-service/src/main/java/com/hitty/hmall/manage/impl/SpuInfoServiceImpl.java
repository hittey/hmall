package com.hitty.hmall.manage.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hitty.hmall.bean.SpuInfo;
import com.hitty.hmall.manage.mapper.SpuInfoMapper;
import com.hitty.hmall.service.SpuInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuInfoServiceImpl implements SpuInfoService {

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Override
    public List<SpuInfo> getSpuList(String catalog3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        return spuInfoMapper.select(spuInfo);
    }

}
