package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domian.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domian.*;
import com.bjpowernode.crm.workbench.service.*;
import com.bjpowernode.crm.workbench.service.impl.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到交易控制器");
        String path = req.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)){
            add(req,resp);
        }else if ("/workbench/transaction/searchActivity.do".equals(path)){
            searchActivity(req,resp);
        }else if ("/workbench/transaction/searchContact.do".equals(path)){
            searchContact(req,resp);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(req,resp);
        }else if ("/workbench/transaction/save.do".equals(path)){
            save(req,resp);
        }else if ("/workbench/transaction/getTranList.do".equals(path)){
            getTranList(req,resp);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(req,resp);
        }else if ("/workbench/transaction/getTranHistoryList.do".equals(path)){
            getTranHistoryList(req,resp);
        }else if ("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(req,resp);
        }else if ("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(req,resp);
        }
    }

    private void getCharts(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("获得交易阶段图表所需要的数据");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map=tranService.getCharts();
        List<Map<String,Object>> dataList = (List<Map<String, Object>>) map.get("dataList");
        for (int i=0;i<dataList.size();i++){
            System.out.println(dataList.get(i).toString()+"---");
        }
        PrintJson.printJsonObj(resp,map);
    }

    private void changeStage(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("更改阶段图标");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id = req.getParameter("id");
        String stage = req.getParameter("stage");
        String money = req.getParameter("money");
        String expectedDate = req.getParameter("expectedDate");
        String editBy=((User)req.getSession().getAttribute("user")).getName();
        String editTime=DateTimeUtil.getSysTime();
        Tran tran=new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        Map<String,String> map1= (Map<String, String>) req.getServletContext().getAttribute("Pmap");
        String possibility = map1.get(stage);
        boolean flag=tranService.changeStage(tran);
        Map<String,Object> map= new HashMap<String,Object>();
        map.put("success",flag);
        map.put("tran",tran);
        map.put("possibility",possibility);
        PrintJson.printJsonObj(resp,map);
    }

    private void getTranHistoryList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("交易历史局部刷新");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        String tranId=req.getParameter("tranId");
        System.out.println(tranId);
        List<TranHistory> tranHistory=tranService.getTranHistoryList(tranId);
        PrintJson.printJsonObj(resp,tranHistory);
    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入交易详细页");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id = req.getParameter("id");
        Tran tran=tranService.detail(id);
        //处理可能性
        String stage=tran.getStage();
        Map<String,String> pMap= (Map<String, String>) this.getServletContext().getAttribute("Pmap");
        String possibility=pMap.get(stage);
        req.setAttribute("possibility",possibility);
        req.setAttribute("tran",tran);
        req.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(req,resp);
    }

    private void getTranList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("刷新交易");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<Tran> tranList=tranService.getTranList();
        PrintJson.printJsonObj(resp,tranList);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("执行添加交易的操作");
        TranService tranService= (TranService) ServiceFactory.getService(new TranServiceImpl());
        String id = UUIDUtil.getUUID();
        String owner =req.getParameter("owner");
        String money =req.getParameter("money");
        String name =req.getParameter("name");
        String expectedDate =req.getParameter("expectedDate");
        String customerName =req.getParameter("customerName");//此处我们暂时只取customerName
        String stage =req.getParameter("stage");
        String type =req.getParameter("type");
        String source =req.getParameter("source");
        String activityId =req.getParameter("activityId");
        String contactsId =req.getParameter("contactsId");
        String createBy =((User)req.getSession().getAttribute("user")).getName();
        String createTime =DateTimeUtil.getSysTime();
        String description =req.getParameter("description");
        String contactSummary =req.getParameter("contactSummary");
        String nextContactTime =req.getParameter("nextContactTime");
        Tran t=new Tran();
        t.setContactsId(contactsId);
        t.setContactSummary(contactSummary);
        t.setDescription(description);
        t.setNextContactTime(nextContactTime);
        t.setOwner(owner);
        t.setSource(source);
        t.setId(id);
        t.setActivityId(activityId);
        t.setStage(stage);
        t.setExpectedDate(expectedDate);
        t.setName(name);
        t.setMoney(money);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setType(type);

        boolean flag=tranService.save(t,customerName);
        if (flag==true) {
            resp.sendRedirect(req.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerName(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("客户姓名自动补全");
        String name = req.getParameter("name");
       CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> strings=customerService.getCustomerName(name);
        PrintJson.printJsonObj(resp,strings);
    }

    private void searchContact(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("在交易中搜索名字查询联系人");
        ContactsService contactsService= (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        String name = req.getParameter("name");
        List<Contacts> contactsList=contactsService.getContactsList(name);
        PrintJson.printJsonObj(resp,contactsList);
    }

    private void searchActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("在交易中通过搜索名字查询市场活动");
        ActivityService activityService= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String name = req.getParameter("name");
        List<Activity> activityListByName = activityService.getActivityListByName(name);
        PrintJson.printJsonObj(resp,activityListByName);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到跳转到交易添加页的操作");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=userService.getUserList();
        req.setAttribute("uList",userList);
        req.getRequestDispatcher("/workbench/transaction/save.jsp").forward(req,resp);
    }
}
