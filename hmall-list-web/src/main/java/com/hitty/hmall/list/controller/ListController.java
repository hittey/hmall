package com.hitty.hmall.list.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hitty.hmall.bean.*;
import com.hitty.hmall.service.ListService;
import com.hitty.hmall.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@CrossOrigin
public class ListController {
    
    @Reference
    private ListService listService;

    @Reference
    private ManageService manageService;
    
    @RequestMapping("list.html")
    public String listData(SkuLsParam skuLsParam, HttpServletRequest request){
        skuLsParam.setPageSize(2);
        SkuLsResult skuLsResult = listService.search(skuLsParam);
        //Object skuJSon = JSON.toJSON(skuLsResult);

        //回显商品列表
        List<SkuLsInfo> skuLsInfoList = skuLsResult.getSkuLsInfoList();


        //回显平台属性，平台属性值
        List<String> valueIdList = skuLsResult.getValueIdList();
        System.out.println(valueIdList.toString());
        List<BaseAttrInfo> attrInfoList = manageService.getAttrInfoList(valueIdList);
        //生成筛选跳转参数
        String uriParam = makeUrilParam(skuLsParam);

        ArrayList<BaseAttrValue> baseAttrValueArrayList = new ArrayList<>();

        for (Iterator<BaseAttrInfo> iterator = attrInfoList.iterator(); iterator.hasNext(); ) {
            BaseAttrInfo baseAttrInfo = iterator.next();
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
            for (BaseAttrValue attrValue : attrValueList) {
                if(skuLsParam.getValueId() != null && skuLsParam.getValueId().length>0){
                    for (String valueId : skuLsParam.getValueId()) {
                        if (valueId.equals(attrValue.getId())){
                            //点击移除平台属性
                            iterator.remove();
                            //生成面包屑，并重新赋值
                            BaseAttrValue baseAttrValue = new BaseAttrValue();
                            String newUrl = makeUrilParam(skuLsParam, valueId);
                            baseAttrValue.setUrlParam(newUrl);
                            baseAttrValue.setValueName(baseAttrInfo.getAttrName()+":"+attrValue.getValueName());
                            baseAttrValueArrayList.add(baseAttrValue);
                        }
                    }
                  }
            }
        }
        request.setAttribute("pageNo",skuLsParam.getPageNo());
        request.setAttribute("totalPages",skuLsResult.getTotalPages());
        request.setAttribute("keyword",skuLsParam.getKeyword());
        request.setAttribute("baseAttrValueArrayList",baseAttrValueArrayList);
        request.setAttribute("urlParam",uriParam);
        request.setAttribute("skuLsInfoList",skuLsInfoList);
        request.setAttribute("attrInfoList",attrInfoList);
        return "list";
    }

    //制作uri参数
    public String makeUrilParam(SkuLsParam skuLsParam,String... exculteParam){
        String uriParam = "";
        //判断keyword非空并且拼接Keyword
        if(skuLsParam.getKeyword() != null&& skuLsParam.getKeyword().length()>0){
            uriParam+="keyword="+skuLsParam.getKeyword();
        }
        //判断catalog3Id非空并且拼接catalog3Id
        if(skuLsParam.getCatalog3Id() != null && skuLsParam.getCatalog3Id().length()>0){
            uriParam+="catalog3Id="+skuLsParam.getCatalog3Id();
          }

        //判断valueId非空并且拼接value
        if(skuLsParam.getValueId() != null && skuLsParam.getValueId().length>0){
            for (String valueId : skuLsParam.getValueId()) {
                if(exculteParam != null && exculteParam.length>0){
                       if(exculteParam[0].equals(valueId)){
                           continue;
                       }
                  }

                if (uriParam.length() > 0) {
                    uriParam += "&";
                }
                uriParam += "valueId=" + valueId;
            }
          }
        return uriParam;
    }
}
