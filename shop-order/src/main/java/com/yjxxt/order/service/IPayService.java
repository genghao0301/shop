package com.yjxxt.order.service;

public interface IPayService {
    /**
     * 支付请求发起
     * @param orderSn 订单号
     * @return
     */
    public String startPayRequest(String orderSn);
}
