package com.hitty.hmall.manage.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.hitty.hmall.bean.*;
import com.hitty.hmall.config.RedisUtil;
import com.hitty.hmall.manage.constant.ManageConst;
import com.hitty.hmall.manage.mapper.*;
import com.hitty.hmall.service.ManageService;
import net.minidev.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.ApplicationScope;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class ManageServiceImpl implements ManageService {

    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;

    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;

    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Autowired
    private BaseSaleAttrMapper saleAttrInfoMapper;

    @Autowired
    private SpuInfoMapper spuInfoMapper;

    @Autowired
    private SpuImageMapper spuImageMapper;

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Autowired
    private SkuInfoMapper skuInfoMapper;

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;

    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(String catalog1Id) {
        BaseCatalog2 baseCatalog2 = new BaseCatalog2();
        baseCatalog2.setCatalog1Id(catalog1Id);
        return this.baseCatalog2Mapper.select(baseCatalog2);
    }

    @Override
    public List<BaseCatalog3> getCatalog3(String catalog2Id) {
        BaseCatalog3 baseCatalog3 = new BaseCatalog3();
        baseCatalog3.setCatalog2Id(catalog2Id);
        return this.baseCatalog3Mapper.select(baseCatalog3);
    }

    @Override
    public List<BaseAttrInfo> getAttrInfoList(String catalog3Id) {
//        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
//        baseAttrInfo.setCatalog3Id(catalog3Id);
//        return baseAttrInfoMapper.select(baseAttrInfo);
        return baseAttrInfoMapper.selectAttrInfoList(catalog3Id);
    }

    @Override
    @Transactional
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //修改，修改情况下baseAttrInfo.id能获取到
        if(baseAttrInfo.getId()!=null && baseAttrInfo.getId().length()>0){
            baseAttrInfoMapper.updateByPrimaryKeySelective(baseAttrInfo);
        } else {
            //添加，添加情况下，baseAttrInfo.id为null
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
        }
        //此时程序进入到编辑界面，程序应清空数据库中attrId下的所有属性值，然后重新插入数据
        //清空
        BaseAttrValue baseAttrValueDel = new BaseAttrValue();
        baseAttrValueDel.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrValueDel);
        //重新在attrId下插入数据
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if(attrValueList!=null && attrValueList.size()>0){
            for (BaseAttrValue baseAttrValue : attrValueList){
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                baseAttrValueMapper.insertSelective(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        return baseAttrValueMapper.select(baseAttrValue);
    }

    @Override
    public BaseAttrInfo getAttrInfo(String attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.select(baseAttrValue);
        baseAttrInfo.setAttrValueList(baseAttrValueList);
        return baseAttrInfo;
    }

    @Override
    public List<BaseSaleAttr> getBaseSaleAttrList() {
        return saleAttrInfoMapper.selectAll();
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuInfo spuInfo) {

        //spuInfo
        spuInfoMapper.insertSelective(spuInfo);
        //spuImage
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(spuImageList != null && spuImageList.size()>0){
            for (SpuImage spuImage : spuImageList) {
               spuImage.setSpuId(spuInfo.getId());
               spuImageMapper.insertSelective(spuImage);
            }
        }
        //spuSaleAttr
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if(spuSaleAttrList!=null && spuSaleAttrList.size()>0){
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insertSelective(spuSaleAttr);

                //spuSaleAttrValue
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if (spuSaleAttrValueList != null &&spuSaleAttrValueList.size()>0){
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insertSelective(spuSaleAttrValue);
                    }
                }
            }
        }


    }

    @Override
    public List<SpuImage> getSpuImageList(SpuImage spuImage) {
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(String spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrList(spuId);
    }

    @Override
    @Transactional
    public void saveSkuInfo(SkuInfo skuInfo) {
//        skuInfo
        skuInfoMapper.insertSelective(skuInfo);
//        skuImage
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if (skuImageList!=null && skuImageList.size()>0){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insertSelective(skuImage);
            }
        }
//        skuAttrValue
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if (skuAttrValueList != null && skuAttrValueList.size()>0){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insertSelective(skuAttrValue);
            }
        }

//        skuSaleAttrValue
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if (skuSaleAttrValueList != null && skuSaleAttrValueList.size()>0){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insertSelective(skuSaleAttrValue);
            }
        }
    }

    @Override
    public SkuInfo getSkuInfo(String skuId) {
        SkuInfo skuInfo = null;
        Jedis jedis = null;
        String skuInfoKey = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_SUPFIX;

        try {
            jedis = redisUtil.getJedis();
            String skuInfoJson = jedis.get(skuInfoKey);
            if(skuInfoJson == null || skuInfoJson.length()==0){
                String skuLock = ManageConst.SKUKEY_PREFIX+skuId+ManageConst.SKUKEY_LOCK;
                String lockKey = jedis.set(skuLock, ManageConst.SKUKEY_LOCK_VALUE, "NX", "PX", ManageConst.SKUKEY_LOCK_TIMEOUT);
                if("OK".equals(lockKey)){
                    skuInfo = getSkuInfoDB(skuId);
                    jedis.setex(skuInfoKey,ManageConst.SKUKEY_TIMEOUT,JSON.toJSONString(skuInfo));
                    jedis.del(skuLock);
                    return skuInfo;
                } else {
                    Thread.sleep(2000);
                    return getSkuInfo(skuId);
                }
            } else{
                skuInfo = JSON.parseObject(skuInfoJson,SkuInfo.class);
                return skuInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return getSkuInfoDB(skuId);
    }

    private SkuInfo getSkuInfoDB(String skuId) {
        SkuInfo skuInfo = new SkuInfo();
        skuInfo = skuInfoMapper.selectByPrimaryKey(skuId);
        skuInfo.setSkuImageList(getSkuImage(skuId));
        SkuAttrValue skuAttrValue = new SkuAttrValue();
        skuAttrValue.setSkuId(skuId);
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.select(skuAttrValue);
        skuInfo.setSkuAttrValueList(skuAttrValueList);
        return skuInfo;
    }

    @Override
    public List<SkuImage> getSkuImage(String skuId) {
        SkuImage skuImage = new SkuImage();
        skuImage.setSkuId(skuId);
        List<SkuImage> skuImageList = skuImageMapper.select(skuImage);
        return skuImageList;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListBySku(SkuInfo skuInfo) {
        return spuSaleAttrMapper.selectSpuSaleAttrListBySku(skuInfo.getId(),skuInfo.getSpuId());
    }

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueLisrBySku(SkuInfo skuInfo) {
        return skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySku(skuInfo.getSpuId());
    }

    @Override
    public List<BaseAttrInfo> getAttrInfoList(List<String> valueIdList) {
        String valueIds = StringUtils.join(valueIdList.toArray(),",");
        System.out.println("valueIds="+valueIds);
        List<BaseAttrInfo> baseAttrInfo= baseAttrInfoMapper.selectAttrInfoListByIds(valueIds);
        return baseAttrInfo;
    }


}
