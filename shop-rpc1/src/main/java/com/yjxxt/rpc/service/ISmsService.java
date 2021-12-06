package com.yjxxt.rpc.service;

public interface ISmsService {

    /**
     * 发送短信验证码
     * @param phone
     */
    public void sendSms(String phone);

    /**
     *
     * @param phone
     * @param type
     *     1:注册
     *     2:用户登录
     *     3:用户下单支付成功
     */
    //public void sendSmsByType(String phone,String type);



}
