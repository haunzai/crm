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
import com.bjpowernode.crm.workbench.domian.Clue;
import com.bjpowernode.crm.workbench.domian.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到线索控制器");
        String path=req.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if("/workbench/clue/save.do".equals(path)){
            save(req,resp);
        }else if("/workbench/clue/pageList.do".equals(path)){
            pageList(req,resp);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(req,resp);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(req,resp);
        }else if("/workbench/clue/unbund.do".equals(path)){
            unbund(req,resp);
        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(req,resp);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(req,resp);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(req,resp);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(req,resp);
        }
    }

    private void convert(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("线索转换");
        String clueId = req.getParameter("clueId");
        //接受是否需要创建交易的标记
        String flag=req.getParameter("flag");
        String createBy=((User)req.getSession().getAttribute("user")).getName();
        Tran t=null;
        if ("a".equals(flag)){
            t=new Tran();
            String money=req.getParameter("money");
            String name=req.getParameter("name");
            String expectedDate=req.getParameter("expectedDate");
            String stage=req.getParameter("stage");
            String activityId=req.getParameter("activityId");
            String id=UUIDUtil.getUUID();

            String createTime=DateTimeUtil.getSysTime();
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setId(id);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
        }
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1=clueService.convert(clueId,t,createBy);
        if (flag1){
            resp.sendRedirect(req.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void getActivityListByName(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("查询活动列表（转换）");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String name = req.getParameter("name");
        List<Activity> activityList=activityService.getActivityListByName(name);
        PrintJson.printJsonObj(resp,activityList);
    }

    private void bund(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("给线索添加关联活动");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String cid = req.getParameter("cid");
        String[] aids = req.getParameterValues("aid");
        Map<String,Object> map1=clueService.bund(cid,aids);
        PrintJson.printJsonObj(resp,map1);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("查询市场活动列表");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String aname = req.getParameter("aname");
        String clueId = req.getParameter("clueId");
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> alist=activityService.getActivityListByNameAndNotByClueId(map);
        PrintJson.printJsonObj(resp,alist);
    }

    private void unbund(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("解除线索与市场活动的关联");
        String id = req.getParameter("id");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=activityService.unbund(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getActivityListByClueId(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据线索Id查询关联市场活动列表");
        ActivityService activityService=(ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String clueId = req.getParameter("clueId");
        List<Activity> list=activityService.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(resp,list);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到线索详细信息页");
        String id=req.getParameter("id");
        ClueService clueService=(ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=clueService.detail(id);
        req.setAttribute("c",clue);
        req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
    }

    private void pageList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("结合条件的分页查询");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        String pageSizestr=req.getParameter("pageSize");
        String pageNostr=req.getParameter("pageNo");
        Integer pageSize=Integer.valueOf(pageSizestr);
        Integer pageNo=Integer.valueOf(pageNostr);
        Integer skipPage=pageSize*(pageNo-1);
        String fullname=req.getParameter("fullname");
        String company=req.getParameter("company");
        String phone=req.getParameter("phone");
        String source=req.getParameter("source");
        String owner=req.getParameter("owner");
        String mphone=req.getParameter("mphone");
        String state=req.getParameter("state");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageSize",pageSize);
        map.put("skipPage",skipPage);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        paginationVO<Clue> vo=clueService.pageList(map);
        PrintJson.printJsonObj(resp,vo);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行线索的添加操作！");
        ClueService clueService= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = new Clue();
        String id=UUIDUtil.getUUID();
        String fullname = req.getParameter("fullname");
        String appellation = req.getParameter("appellation");
        String owner = req.getParameter("owner");
        String company = req.getParameter("company");
        String job = req.getParameter("job");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String website = req.getParameter("website");
        String mphone = req.getParameter("mphone");
        String state = req.getParameter("state");
        String source = req.getParameter("source");
        String description = req.getParameter("description");
        String contactSummary = req.getParameter("contactSummary");
        String nextContactTime = req.getParameter("nextContactTime");
        String address = req.getParameter("address");
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)req.getSession().getAttribute("user")).getName();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        boolean flag=clueService.save(clue);
        PrintJson.printJsonFlag(resp,flag);
    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("取得用户信息列表");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=userService.getUserList();
        PrintJson.printJsonObj(resp,uList);
    }
}
