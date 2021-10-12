package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domian.DicValue;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("服务器缓存处理数据字典开始！");
        ServletContext application = sce.getServletContext();
        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String , List<DicValue>> map=ds.getAll();
        //将map解析为上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String s:set){
            application.setAttribute(s,map.get(s));
        }
        System.out.println("服务器缓存处理数据字典开始！");

        //解析Stage2Possibility文件
        //将properties读取为map集合
        Map<String, String> Pmap = new HashMap<String, String>();
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e= rb.getKeys();
        while(e.hasMoreElements()){
            String key = e.nextElement();
            String string = rb.getString(key);
            Pmap.put(key,string);
        }
        application.setAttribute("Pmap",Pmap);
    }
}
