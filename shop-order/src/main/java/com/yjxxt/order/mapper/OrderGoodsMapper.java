package com.yjxxt.order.mapper;

import com.yjxxt.order.base.BaseMapper;
import com.yjxxt.order.pojo.OrderGoods;
import com.yjxxt.order.pojo.OrderGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderGoodsMapper extends BaseMapper<OrderGoods,OrderGoodsExample> {
    /**
     * 批量保存订单商品记录
     * @param orderGoods
     * @return
     */
    public int insertOrderGoodsBatch(List<OrderGoods> orderGoods);
}