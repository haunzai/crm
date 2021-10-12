package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domian.Tran;
import com.bjpowernode.crm.workbench.domian.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran t, String customerName);

    List<Tran> getTranList();

    Tran detail(String id);

    List<TranHistory> getTranHistoryList(String tranId);

    boolean changeStage(Tran tran);

    Map<String, Object> getCharts();
}
