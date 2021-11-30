package com.yjxxt.manager.interceptors;

import com.yjxxt.common.result.CookieUtil;
import com.yjxxt.sso.pojo.Admin;
import com.yjxxt.sso.service.ISSOService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private ISSOService ssoService;

    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOperations;



    @Value("${user.ticket}")
    private String userTicket;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
          1.获取cookie 中用户票据
          2.判断票据是否存在
             不存在 用户未登录 跳转到登录页面
             存在 获取用户信息存入session 放行
         */
        String ticket= CookieUtil.getCookieValue(request,"userTicket");
        Admin admin= ssoService.validateTicket(ticket);
        if(null ==admin){
           response.sendRedirect(request.getContextPath()+"/login");
          return  false;
        }
        // 重新存入用户信息到redis 30分钟内有效
        valueOperations.set(userTicket+":"+ticket,admin,30, TimeUnit.MINUTES);
        request.getSession().setAttribute("user",admin);
        return true;
    }
}