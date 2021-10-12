package com.bjpowernode.crm.workbench.dao;


import com.bjpowernode.crm.workbench.domian.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    int getCountByCondition(Map<String, Object> map);

    Clue detail(String id);

    Clue getClueById(String clueId);

    int delete(String clueId);
}
