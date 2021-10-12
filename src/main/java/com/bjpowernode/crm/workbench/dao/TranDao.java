package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domian.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    List<Tran> getTranList();

    Tran detail( String id);

    int changeStage(Tran tran);

    int getTotal();

    List<Map<String, Object>> getListByGroup();
}
