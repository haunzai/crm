package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domian.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<String> getCustomerName(String name);

}
