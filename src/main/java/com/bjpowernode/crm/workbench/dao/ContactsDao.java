package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domian.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts contacts);

    List<Contacts> getContactsList(String name);
}
