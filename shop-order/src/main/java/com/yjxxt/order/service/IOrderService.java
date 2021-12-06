package com.yjxxt.order.service;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.order.pojo.Order;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface IOrderService {
    /**
     * 保存订单信息
     * @param userId 当前用户id
     * @return
     */
    public BaseResult saveOrder(Integer userId);

    /**
     * 根据订单编号查询订单记录
     * @param orderSn
     * @return
     */
    public Order queryOrderByOrderSn(String orderSn);

    /**
     * 支付同步回调业务方法
     * @param outTradeNo  订单编号
     * @param totalAmount 订单支付金额
     * @param tradeNo    交易流水
     * @param request    请求
     * @return
     */
    public BaseResult returnCallback(String outTradeNo,
                                     BigDecimal totalAmount,
                                     String tradeNo,
                                     HttpServletRequest request);


    /**
     * 支付异步回调
     * @param outTradeNo  订单编号
     * @param totalAmount  订单金额
     * @param tradeNo     交易流水号
     * @param tradeStatus  校验状态
     * @param request     请求
     * @return
     */
    public BaseResult notifyCallback(String outTradeNo,
                                     BigDecimal totalAmount,
                                     String tradeNo,
                                     String tradeStatus,
                                     HttpServletRequest request);
}
