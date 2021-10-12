package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domian.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> getContactsList(String name);
}
