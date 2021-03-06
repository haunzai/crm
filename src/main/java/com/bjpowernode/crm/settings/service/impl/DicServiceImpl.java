package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domian.DicType;
import com.bjpowernode.crm.settings.domian.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicValueDao dicValueDao= SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
    private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map=new HashMap<String, List<DicValue>>();
       List<DicType> dtList = dicTypeDao.getTypeList();
       for (DicType dt:dtList){
           String code=dt.getCode();
           List<DicValue> dvList=dicValueDao.getListByCode(code);
           map.put(code,dvList);
       }
       return map;
    }
}
