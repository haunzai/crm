package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.settings.domian.User;
import com.bjpowernode.crm.vo.paginationVO;
import com.bjpowernode.crm.workbench.domian.Activity;
import com.bjpowernode.crm.workbench.domian.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    paginationVO<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String,Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String id);

    boolean Activityremarkdelete(String id);

    boolean Activityremarksave(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    boolean unbund(String id);

    List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map);

    List<Activity> getActivityListByName(String name);
}
