package com.bjpowernode.crm.workbench.dao;


import com.bjpowernode.crm.workbench.domian.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbund(String id);

    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> getcar(String clueId);

    int delete(ClueActivityRelation car);
}
