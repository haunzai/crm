package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domian.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String id);

    int Activityremarkdelete(String id);

    int Activityremarksave(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark ar);
}
