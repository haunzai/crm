package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domian.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.paginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.domian.Activity;
import com.bjpowernode.crm.workbench.domian.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
   private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
   private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    public boolean save(Activity activity) {
        boolean flag=true;
        int count=activityDao.save(activity);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public paginationVO<Activity> pageList(Map<String, Object> map) {
        //取得total
       int total = activityDao.getTotalByCondition(map);
        //取得activityList
       List<Activity> activityList= activityDao.getActivityListByCondition(map);
       //将total和activityList放入到vo中
        paginationVO<Activity> vo = new paginationVO<Activity>();
        vo.setDataList(activityList);
        vo.setTotal(total);
        return vo;
    }

    public boolean delete(String[] ids) {
        boolean flag=true;
        //查询出应该删除的数量
        int count1=activityRemarkDao.getCountByAids(ids);
        //实际删除的对象
        int count2=activityRemarkDao.deleteByAids(ids);
        if (count1!=count2){
            flag=false;
        }
        //删除市场活动
        int count3=activityDao.delete(ids);
        if (count3!=ids.length){
            flag=false;
        }
        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> userList = userDao.getUserList();
        List<Activity> activity=activityDao.getById(id);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uList",userList);
        map.put("ac",activity);
        return map;
    }

    public boolean update(Activity activity) {
        boolean flag=true;
        int count=activityDao.update(activity);
        if (count!=1){
            flag=false;
        }
        return  flag;
    }

    public Activity detail(String id) {

        Activity a=activityDao.detail(id);
        System.out.println(a);
        return a;
    }

    public List<ActivityRemark> getRemarkListByAid(String id) {
        List<ActivityRemark> ar=  activityRemarkDao.getRemarkListByAid(id);
        return ar;
    }

    public boolean Activityremarkdelete(String id) {
        boolean flag=true;
        int count=activityRemarkDao.Activityremarkdelete(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean Activityremarksave(ActivityRemark activityRemark) {
        boolean flag=true;
        int count=activityRemarkDao.Activityremarksave(activityRemark);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean updateRemark(ActivityRemark ar) {
        boolean flag=true;
        int count=activityRemarkDao.updateRemark(ar);
        if (count!=1){
            flag=false;
        }
    return flag;
    }

    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> activityList=activityDao.getActivityListByClueId(clueId);
        return activityList;
    }

    public boolean unbund(String id) {
        boolean flag=true;
        int count=clueActivityRelationDao.unbund(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, Object> map) {
        List<Activity> activityList=activityDao.getActivityListByNameAndNotByClueId(map);
        return activityList;
    }

    public List<Activity> getActivityListByName(String name) {
        List<Activity> list=activityDao.getActivityListByName(name);
        return list;
    }
}
