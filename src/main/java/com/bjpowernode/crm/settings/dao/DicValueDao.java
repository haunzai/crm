package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domian.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
