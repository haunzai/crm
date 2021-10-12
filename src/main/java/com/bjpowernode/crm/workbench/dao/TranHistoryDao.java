package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domian.TranHistory;

import java.util.List;


public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getTranHistoryList(String tranId);
}
