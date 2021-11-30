package com.yjxxt.portal.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICookieService {

    /**
     * 设置cookie
     * @param request
     * @param response
     * @param ticket
     * @return
     */
    void setCookie(HttpServletRequest request, HttpServletResponse response, String ticket);



}
