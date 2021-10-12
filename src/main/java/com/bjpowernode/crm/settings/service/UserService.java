package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.loginException;
import com.bjpowernode.crm.settings.domian.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String md5, String ip) throws loginException;

    List<User> getUserList();
}
