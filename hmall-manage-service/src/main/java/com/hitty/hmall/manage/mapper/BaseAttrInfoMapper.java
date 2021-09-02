package com.hitty.hmall.manage.mapper;

import com.hitty.hmall.bean.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {
    List<BaseAttrInfo> selectAttrInfoList(String catalog3Id);

    List<BaseAttrInfo> selectAttrInfoListByIds(@Param("valueIds") String valueIds);
}
