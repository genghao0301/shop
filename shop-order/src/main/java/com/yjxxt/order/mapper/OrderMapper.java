package com.yjxxt.order.mapper;

import com.yjxxt.order.base.BaseMapper;
import com.yjxxt.order.pojo.Order;
import com.yjxxt.order.pojo.OrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper extends BaseMapper<Order,OrderExample> {
}