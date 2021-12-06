package com.yjxxt.order.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.yjxxt.common.enums.*;
import com.yjxxt.common.result.BaseResult;
import com.yjxxt.order.config.AlipayConfig;
import com.yjxxt.order.mapper.OrderGoodsMapper;
import com.yjxxt.order.mapper.OrderMapper;
import com.yjxxt.order.pojo.Order;
import com.yjxxt.order.pojo.OrderExample;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
//        order.setAddTime(((Long)System.currentTimeMillis()).intValue());
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

    @Override
    public Order queryOrderByOrderSn(String orderSn) {
        //设置查询条件
        OrderExample example=new OrderExample();
        example.createCriteria().andOrderSnEqualTo(orderSn);
        List<Order> orders =orderMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(orders)){
            return orders.get(0);
        }
        return null;
    }

    @Override
    public BaseResult returnCallback(String outTradeNo,
                                     BigDecimal totalAmount,
                                     String tradeNo,
                                     HttpServletRequest request) {
        try {
            //获取支付宝GET过来反馈信息
            Map<String,String> params = new HashMap<String,String>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
            if(signVerified) {
                System.out.println("请求合法...");
                /**
                 * 订单状态更新
                 *    参数: 订单编号  支付宝支付流水  订单金额
                 *   订单更新思路
                 *     I.参数校验
                 *       1.订单记录查询
                 *       2.记录存在校验
                 *       3.订单支付状态校验(未支付)
                 *       4.订单金额校验
                 *     II.订单状态更新
                 */
                Order order = this.queryOrderByOrderSn(outTradeNo);
                if(null == order){
                    return BaseResult.error("订单记录不存在!");
                }

                if(order.getPayStatus().equals(PayStatus.has_payed.getStatus())){
                    return BaseResult.success();
                }

                if(order.getTotalAmount().compareTo(totalAmount) !=0){
                    return BaseResult.error("订单金额异常!");
                }

                // 设置订单支付状态为已支付
                order.setPayStatus(PayStatus.has_payed.getStatus());
                order.setShippingStatus(ShipStatus.has_send.getStatus());
                order.setPayCode(tradeNo);
                order.setPayName("支付宝支付");
                //order.setPayTime(new Date());
                if(orderMapper.updateByPrimaryKeySelective(order) !=1){
                    return  BaseResult.error("订单支付异常，请联系客服!");
                }
                return BaseResult.success();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BaseResult notifyCallback(String outTradeNo,
                                     BigDecimal totalAmount,
                                     String tradeNo, String tradeStatus, HttpServletRequest request) {
        try {
            //获取支付宝GET过来反馈信息
            Map<String,String> params = new HashMap<String,String>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
            if(signVerified) {
                System.out.println("请求合法...");
                // 支付成功时 更新订单状态
                if(tradeStatus.equals("TRADE_SUCCESS")){
                    /**
                     * 订单状态更新
                     *    参数: 订单编号  支付宝支付流水  订单金额
                     *   订单更新思路
                     *     I.参数校验
                     *       1.订单记录查询
                     *       2.记录存在校验
                     *       3.订单支付状态校验(未支付)
                     *       4.订单金额校验
                     *     II.订单状态更新
                     */
                    Order order = this.queryOrderByOrderSn(outTradeNo);
                    if(null == order){
                        return BaseResult.error("订单记录不存在!");
                    }

                    if(order.getPayStatus().equals(PayStatus.has_payed.getStatus())){
                        return BaseResult.success();
                    }

                    if(order.getTotalAmount().compareTo(totalAmount) !=0){
                        return BaseResult.error("订单金额异常!");
                    }

                    // 设置订单支付状态为已支付
                    order.setPayStatus(PayStatus.has_payed.getStatus());
                    order.setShippingStatus(ShipStatus.has_send.getStatus());
                    order.setPayCode(tradeNo);
                    order.setPayName("支付宝支付");
                    //order.setPayTime(new Date());
                    if(orderMapper.updateByPrimaryKeySelective(order) !=1){
                        return  BaseResult.error("订单支付异常，请联系客服!");
                    }
                    return BaseResult.success();
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return BaseResult.error();

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
