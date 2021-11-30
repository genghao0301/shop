package com.yjxxt.manager.service.impl;

import com.yjxxt.common.result.CookieUtil;
import com.yjxxt.manager.service.ICookieService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieServiceImpl implements ICookieService {
    @Override
    public void setCookie(HttpServletRequest request, HttpServletResponse response, String ticket) {
        CookieUtil.setCookie(request,response,"userTicket",ticket);
    }
}
