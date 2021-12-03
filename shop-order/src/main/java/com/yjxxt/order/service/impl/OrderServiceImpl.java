package com.yjxxt.order.service.impl;

import com.yjxxt.common.enums.*;
import com.yjxxt.common.result.BaseResult;
import com.yjxxt.order.mapper.OrderGoodsMapper;
import com.yjxxt.order.mapper.OrderMapper;
import com.yjxxt.order.pojo.Order;
import com.yjxxt.order.pojo.OrderGoods;
import com.yjxxt.order.service.IOrderService;
import com.yjxxt.rpc.service.ICartService;
import com.yjxxt.rpc.vo.CartResult;
import com.yjxxt.rpc.vo.CartVo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class OrderServiceImpl implements IOrderService {
    //注入远程购物车  rpc模块
    @Reference(version = "1.0")
    private ICartService cartService;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderGoodsMapper orderGoodsMapper;
    //调用它来实现订单编号的自增
    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;
    @Value("${redis.order.increment}")
    private String incrementKey;

    //添加  需要提交事务
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BaseResult saveOrder(Integer userId) {
        /**
         * 1.涉及表
         *    t_order  订单表
         *    t_order_goods  订单商品表
         *    t_goods 商品表
         * 2.待下单商品记录查询
         *     根据用户id 从购物车查询商品数据
         * 3.订单以及订单商品记录保存
         *    先保存订单记录 返回订单主键
         *    后保存订单商品记录(批量保存)
         * 4. 清除购物车商品
         * 5.返回保存结果
         */
        //获取购物车列表
        CartResult cartResult = cartService.getCartListInfo(userId);
        //创建订单对象
        Order order = new Order();
        //生成订单编号
        String orderSn="geng_"+
                new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                +getIncrement(incrementKey);
        //设置订单号
        order.setOrderSn(orderSn);
        //设置用户id
        order.setUserId(userId);
        //设置订单状态
        order.setOrderStatus(OrderStatus.has_confirm.getStatus());
        //设置发货状态
        order.setShippingStatus(ShipStatus.no_send.getStatus());
        //设置支付状态
        order.setPayStatus(PayStatus.no_pay.getStatus());
        //设置商品价格
        order.setGoodsPrice(cartResult.getTotalPrice());
        //设置应付总金额
        order.setOrderAmount(cartResult.getTotalPrice());
        //设置订单总价
        order.setTotalAmount(cartResult.getTotalPrice());
        //数据库格式有误  范围超
        order.setAddTime(((Long)System.currentTimeMillis()).intValue());
//        System.out.println("测试");
//        System.out.println(((Long)System.currentTimeMillis()).intValue());
        //获取订单添加结果
        int result = orderMapper.insertSelective(order);
        if(result<=0){
            return  BaseResult.error("订单提交失败!");
        }

        // 保存订单商品记录
        List<OrderGoods> orderGoods =new ArrayList<OrderGoods>();
        for(CartVo cartVo: cartResult.getCartList()){
            OrderGoods temp =new OrderGoods();
            temp.setOrderId(order.getOrderId());
            temp.setGoodsId(cartVo.getGoodsId());
            temp.setGoodsName(cartVo.getGoodsName());
            temp.setGoodsPrice(cartVo.getMarketPrice());
            temp.setGoodsNum(cartVo.getGoodsNum().shortValue());
            temp.setPromType(PromTypeStatus.normal.getStatus());
            temp.setIsSend(SendStatus.no_pay.getStatus());
            orderGoods.add(temp);
        }
        //批量添加商品信息到数据库
        result =orderGoodsMapper.insertOrderGoodsBatch(orderGoods);
        if(result != orderGoods.size()){
            return BaseResult.error("订单提交失败!");
        }

        // 清除购物车商品
        cartService.clearCart(userId);

        // 总金额  订单编号
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("totalAmount",cartResult.getTotalPrice());
        map.put("orderSn",orderSn);
        BaseResult baseResult =BaseResult.success();
        baseResult.setResult(map);
        return baseResult;
    }

    /**
     * 生成redis自增key
     *
     * @param key
     * @return
     */
    private Long getIncrement(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return entityIdCounter.getAndIncrement();
    }
}
