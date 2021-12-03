package com.yjxxt.order.service;

import com.yjxxt.common.result.BaseResult;

public interface IOrderService {
    /**
     * 保存订单信息
     * @param userId 当前用户id
     * @return
     */
    public BaseResult saveOrder(Integer userId);
}
