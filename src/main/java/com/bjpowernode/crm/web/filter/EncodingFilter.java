package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到字符编码的过滤器");
        //处理post请求中文参数乱码
        servletRequest.setCharacterEncoding("UTF-8");
        //处理响应流响应中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");
        //将请求放行
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
