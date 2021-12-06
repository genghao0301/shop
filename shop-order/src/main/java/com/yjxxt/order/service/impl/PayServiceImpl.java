package com.yjxxt.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.yjxxt.order.config.AlipayConfig;
import com.yjxxt.order.pojo.Order;
import com.yjxxt.order.service.IOrderService;
import com.yjxxt.order.service.IPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class PayServiceImpl implements IPayService {
    @Resource
    private IOrderService orderService;

    @Override
    public String startPayRequest(String orderSn) {
        Order order = orderService.queryOrderByOrderSn(orderSn);
        if (null == order) {
            System.out.println("订单记录不存在!");
            return null;
        }
        try {
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset,
                    AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
            BigDecimal total_amount = BigDecimal.valueOf(2999L);
            String subject = "订单支付";
            alipayRequest.setBizContent("{\"out_trade_no\":\"" + orderSn + "\","
                    + "\"total_amount\":\"" + order.getTotalAmount() + "\","
                    + "\"subject\":\"" + subject + "\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            return  alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
