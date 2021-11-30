package com.yjxxt.sso.service;

import com.yjxxt.sso.pojo.Admin;

public interface ISSOService {

    /**
     * 登录方法
     * @param userName  用户名
     * @param password  密码
     * @return
     */
    public String login(String userName,String password);


    /**
     * 用户票据校验
     * @param ticket
     * @return
     */
    public Admin validateTicket(String ticket);


    /**
     * 用户退出
     * @param ticket
     */
    public void logout(String ticket);

}
