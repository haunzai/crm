package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domian.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int deleteByClueId(ClueRemark clueRemark);
}
