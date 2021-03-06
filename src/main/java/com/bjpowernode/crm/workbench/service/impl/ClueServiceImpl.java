package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.paginationVO;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domian.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao=  SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    public boolean save(Clue clue) {
        boolean flag=true;
        int count=clueDao.save(clue);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public paginationVO<Clue> pageList(Map<String, Object> map) {
        List<Clue> list=clueDao.getClueListByCondition(map);
        int total=clueDao.getCountByCondition(map);
        paginationVO<Clue> vo = new paginationVO<Clue>();
        vo.setTotal(total);
        vo.setDataList(list);
        return vo;
    }

    public Clue detail(String id) {
        Clue clue=clueDao.detail(id);
        return clue;
    }

    public Map<String,Object> bund(String cid, String[] aids) {
        int i=0,j=0;
        boolean flag=true;
        for (String aid:aids){
            ClueActivityRelation car=new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);
            i=clueActivityRelationDao.bund(car);
            if (i==1){
                j++;
            }else{
                flag=false;
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("count",j);
        map.put("success",flag);
        return map;
    }

    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime= DateTimeUtil.getSysTime();
        boolean flag=true;
        //1.????????????id??????????????????
        Clue clue=clueDao.getClueById(clueId);
        //2.?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        String company=clue.getCompany();
        Customer customer=customerDao.getCustomerByName(company);
        //??????cus???null,???????????????????????????????????????????????????
        if (customer==null){
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setDescription(clue.getDescription());
            customer.setName(company);
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());
            customer.setOwner(clue.getOwner());
            int count1=customerDao.save(customer);
            if (count1 != 1) {
                flag=false;
            }
        }
        //3.???????????????????????????????????????
        Contacts contacts=new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAddress(clue.getAddress());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setSource(clue.getSource());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setOwner(clue.getOwner());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setCustomerId(customer.getId());
        contacts.setAppellation(clue.getAppellation());
        //???????????????
        int count2=contactsDao.save(contacts);
        if (count2!=1){
            flag=false;
        }
        //4.???????????????????????????????????????????????????
        List<ClueRemark> crList=clueRemarkDao.getListByClueId (clueId);
        for (ClueRemark clueRemark:crList){
            String noteContent = clueRemark.getNoteContent();
            //????????????
            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count3=customerRemarkDao.save(customerRemark);
            if (count3!=1){
                flag=false;
            }
            //???????????????
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count4=contactsRemarkDao.save(contactsRemark);
            if (count4!=1){
                flag=false;
            }
        }
        //5.????????????????????????????????????????????????????????????????????????
        List<ClueActivityRelation> cars=clueActivityRelationDao.getcar(clueId);
        for (ClueActivityRelation car:cars){
            String activityId = car.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            int count5 =contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5!=1){
                flag=false;
            }
        }
        //6.????????????????????????????????????????????????
        if (t!=null){
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(contacts.getId());
            int count6=tranDao.save(t);
            if (count6!=1){
                flag=false;
            }
            //7.???????????????????????????????????????????????????
            TranHistory tranHistory = new TranHistory();
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            int count7=tranHistoryDao.save(tranHistory);
            if (count7!=1) {
                flag=false;
            }
        }
        //8.??????????????????
        for (ClueRemark clueRemark:crList){
            int count8 =clueRemarkDao.deleteByClueId(clueRemark);
            if (count8!=1){
                flag=false;
            }
        }
        //9.????????????????????????????????????
        for(ClueActivityRelation car:cars){
            int count9 =clueActivityRelationDao.delete(car);
            if (count9!=1){
                flag=false;
            }
        }
        //10.????????????
        int count10=clueDao.delete(clueId);
        if (count10!=1){
            flag=false;
        }

        return flag;
    }

}
