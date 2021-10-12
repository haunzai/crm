package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domian.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.paginationVO;
import com.bjpowernode.crm.workbench.domian.Activity;
import com.bjpowernode.crm.workbench.domian.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path=req.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if ("/workbench/activity/save.do".equals(path)){
            save(req,resp);
        }else if ("/workbench/activity/pageList.do".equals(path)){
            pageList(req,resp);
        }else if ("/workbench/activity/delete.do".equals(path)){
            delete(req,resp);
        }else if ("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(req,resp);
        }else if ("/workbench/activity/update.do".equals(path)){
            update(req,resp);
        }else if ("/workbench/activity/detail.do".equals(path)){
            detail(req,resp);
        }else if ("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(req,resp);
        }else if ("/workbench/activity/Activityremarkdelete.do".equals(path)){
            Activityremarkdelete(req,resp);
        }else if ("/workbench/activity/Activityremarksave.do".equals(path)){
            Activityremarksave(req,resp);
        }else if ("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(req,resp);
        }
    }

    private void updateRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行修改操作");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = req.getParameter("id");
        String noteContent = req.getParameter("noteContent");
        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)req.getSession().getAttribute("user")).getName();
        String editFlag="1";
        ActivityRemark ar = new ActivityRemark();
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setEditFlag(editFlag);
        ar.setId(id);
        ar.setNoteContent(noteContent);
        boolean flag=activityService.updateRemark(ar);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(resp,map);
    }

    private void Activityremarksave(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("添加备注信息表");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)req.getSession().getAttribute("user")).getName();
        String editFlag="0";
        ActivityRemark activityRemark=new ActivityRemark();
        activityRemark.setActivityId(activityId);
        activityRemark.setCreateBy(createBy);
        activityRemark.setCreateTime(createTime);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        boolean flag=activityService.Activityremarksave(activityRemark);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",activityRemark);
        PrintJson.printJsonObj(resp,map);

    }

    private void Activityremarkdelete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("删除备注信息表");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id = req.getParameter("id");
        boolean flag=activityService.Activityremarkdelete(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据市场活动的id来取得备注信息表");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id=req.getParameter("ActivityId");
        List<ActivityRemark> list=activityService.getRemarkListByAid(id);
        PrintJson.printJsonObj(resp,list);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到详细信息页的操作");
        String id=req.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity ac=activityService.detail(id);
        req.setAttribute("ac",ac);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行修改操作");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String id= req.getParameter("id");
        String owner= req.getParameter("owner");
        String name= req.getParameter("name");
        String startDate=req.getParameter("startDate");
        String endDate= req.getParameter("endDate");
        String cost= req.getParameter("cost");
        String description= req.getParameter("description");
        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)req.getSession().getAttribute("user")).getName();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);
        boolean flag =activityService.update(activity);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到查询用户列表和市场活动信息");
        String id=req.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map= activityService.getUserListAndActivity(id);
        PrintJson.printJsonObj(resp,map);
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到市场活动列表删除操作");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //取多个属性名相同可以用数组进行接收
        String ids[]=req.getParameterValues("id");
        boolean flag= activityService.delete(ids);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询和分页查询）");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String pageNostr = req.getParameter("pageNo");
        //每页的记录数
        String pageSizestr = req.getParameter("pageSize");
        Integer pageNo=Integer.valueOf(pageNostr);
        Integer pageSize=Integer.valueOf(pageSizestr);
        //略过的页数
        Integer skipCount=pageSize*( pageNo-1);
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("skipCount",skipCount);
        map.put("name",name);
        map.put("pageSize",pageSize);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        paginationVO<Activity> vo=activityService.pageList(map);
        PrintJson.printJsonObj(resp,vo);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动的添加操作");
        String id= UUIDUtil.getUUID();
        String owner=req.getParameter("owner");
        String name=req.getParameter("name");
        String startDate=req.getParameter("startDate");
        String endDate=req.getParameter("endDate");
        String cost=req.getParameter("cost");
        String description=req.getParameter("description");
        String createTime= DateTimeUtil.getSysTime();
        //从request里取出session,在从session中取出user,进行强转，最后从user取出name
        String createBy=((User)req.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();

        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag= activityService.save(activity);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=userService.getUserList();
        PrintJson.printJsonObj(resp,uList);
    }
}
