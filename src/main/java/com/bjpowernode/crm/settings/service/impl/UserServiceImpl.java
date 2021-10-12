package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.loginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domian.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String md5, String ip) throws loginException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",md5);
        User user=userDao.login(map);
        if (user==null){
            throw new loginException("账号或密码错误！");
        }
        //验证失效时间
        String expire=user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();
        if (expire.compareTo(currentTime)<0){
            throw new loginException("账号已失效");
        }
        //判断锁定状态
        String statue=user.getLockState();
        if ("0".equals(statue)){
            throw new loginException("账号已被冻结！");
        }
        //判断ip地址
        String ips=user.getAllowIps();
        if (!ips.contains(ip)){
            System.out.println(ip);
            throw new loginException("该IP不允许访问");
        }
        return user;
    }

    public List<User> getUserList() {
        List<User> uList=userDao.getUserList();
        return uList;
    }
}
