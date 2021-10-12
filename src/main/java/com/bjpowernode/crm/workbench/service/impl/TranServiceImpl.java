package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domian.Customer;
import com.bjpowernode.crm.workbench.domian.Tran;
import com.bjpowernode.crm.workbench.domian.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public boolean save(Tran t, String customerName) {
        boolean flag = true;
        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(t.getContactSummary());
            customer.setNextContactTime(t.getNextContactTime());
            customer.setOwner(t.getOwner());
            int count0 = customerDao.save(customer);
            if (count0 != 1) {
                flag = false;
            }
        }
        String customerId = customer.getId();
        t.setCustomerId(customerId);
        int count1 = tranDao.save(t);
        if (count1 != 1) {
            flag = false;
        } else {
            TranHistory tranHistory = new TranHistory();
            tranHistory.setTranId(t.getId());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(t.getStage());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setCreateBy(t.getCreateBy());
            int count2 = tranHistoryDao.save(tranHistory);
            if (count2 != 1) {
                flag = false;
            }
        }
        return flag;
    }

    public List<Tran> getTranList() {
        List<Tran> tranList=tranDao.getTranList();
        return tranList;
    }

    public Tran detail(String id) {
        Tran tran=tranDao.detail(id);
        return tran;
    }

    public List<TranHistory> getTranHistoryList(String tranId) {
        List<TranHistory> tranHistoryList = tranHistoryDao.getTranHistoryList(tranId);
        return tranHistoryList;
    }

    public boolean changeStage(Tran tran) {
        boolean flag=true;
        int count= tranDao.changeStage(tran);
        if (count!=1){
            flag=false;
        }else{
            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(tran.getCreateBy());
            tranHistory.setCreateTime(tran.getCreateTime());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            tranHistory.setTranId(tran.getId());
            int count2=tranHistoryDao.save(tranHistory);
            if (count2!=1){
                flag=false;
            }
        }
        return  flag;
    }

    public Map<String, Object> getCharts() {
        int count=tranDao.getTotal();
        List<Map<String,Object>> dataList=tranDao.getListByGroup();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total",count);
        map.put("dataList",dataList);
        return map;
    }

}
