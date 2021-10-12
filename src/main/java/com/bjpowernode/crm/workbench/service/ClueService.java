package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.paginationVO;
import com.bjpowernode.crm.workbench.domian.Clue;
import com.bjpowernode.crm.workbench.domian.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    paginationVO<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    Map<String,Object> bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
