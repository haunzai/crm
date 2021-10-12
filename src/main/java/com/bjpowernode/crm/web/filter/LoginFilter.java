package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domian.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("验证有没有登录过的过滤器");
        //向下造型需要强转（父转子），向上造型不需要
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        String path= request.getServletPath();
        //放行登录相关的资源
        if ("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            //获取session
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user!=null){
                //使用绝对路径，‘/’项目名+路径
                filterChain.doFilter(servletRequest,servletResponse);
            }else{
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }}

    }
}
