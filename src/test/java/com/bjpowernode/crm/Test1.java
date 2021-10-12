package com.bjpowernode.crm;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {
    public static void main(String[] args) {
        //验证失效时间
        String expireTime="2021-08-02 10:10:10";
        //输出简单时间格式
//        Date date=new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        String format = sdf.format(date);
//        System.out.println(format);
        //时间差
//        String currentTime= DateTimeUtil.getSysTime();
//        int count=currentTime.compareTo(expireTime);
//        System.out.println(count);
        //判断账号是否被锁定
//        int loginState=0;
//        if ("0".equals(loginState)){
//            System.out.println("账号被锁定！");
//        }else{
//            System.out.println("账号正常！");
//        }
//        //判断ip地址是否符合要求
//        String ip= "192.168.1.1";
//        String ips="192.168.1.1,192.168.1.2";
//        if (ips.contains(ip)){
//            System.out.println("ip正常！");
//        }else{
//            System.out.println("ip被限制");
//        }
        //加密
        String pwd="925.love";
        String pwd1= MD5Util.getMD5(pwd);
        System.out.println(pwd1);

    }
}
