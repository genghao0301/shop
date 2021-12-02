package com.yjxxt.rpc.service.impl;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.rpc.mapper.GoodsMapper;
import com.yjxxt.rpc.pojo.Goods;
import com.yjxxt.rpc.pojo.GoodsExample;
import com.yjxxt.rpc.service.ICartService;
import com.yjxxt.rpc.vo.CartResult;
import com.yjxxt.rpc.vo.CartVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *实现类
 */
@Service(version = "1.0")
public class CartServiceImpl implements ICartService {
    @Resource
    private GoodsMapper goodsMapper;

    @Value("${user.cartKey}")
    private String userCart;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String,String,Object> hashOperations;

    @Override
    public BaseResult saveCartVo(CartVo cartVo, Integer userId) {
        /**
         * 1.参数校验
         *    商品记录存在性校验
         *    商品数量>=1
         * 2.加入商品到购物车
         *     2.1 购物车不存在该商品
         *         put(key,goodsId,CartVo)
         *     2.2 购物车存在该商品
         *        原来商品数量+当前商品数量 存入redis
         *3.返回结果
         */
        //获取添加的商品id
        Integer goodsId = cartVo.getGoodsId();
        //判断是否有勾选商品
        if (null==goodsId || goodsId==0){

            return BaseResult.error("商品记录不存在，请选择商品添加！");
        }
        //创建goods查询条件
        GoodsExample example = new GoodsExample();
        //添加条件
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        //通过条件查询结果集返回
        List<Goods> goodsList = goodsMapper.selectByExample(example);
//        List<Goods> goodsList = goodsMapper.selectByExample(new GoodsExample());
        for (Goods goods:goodsList){
            System.out.println("--->"+goods);
        }
        System.out.println("测试结束！");
        //判断结果集
        if (CollectionUtils.isEmpty(goodsList)){
            return BaseResult.error("商品记录不存在！");
        }
        //判断添加商品的数量
        if (cartVo.getGoodsNum()<=0){
            return BaseResult.error("商品数量异常！");
        }
        //创建购物车key
        String key = userCart+":"+userId;
        CartVo temp=cartVo;
        //给商品加添加时间
        temp.setAddTime(new Date());
        //*******************************
        String goodsName = goodsList.get(0).getGoodsName();
        System.out.println(goodsName);
        temp.setGoodsName(goodsName);
        //**************************
        //创建一个map类型
        Map<String, Object> map = hashOperations.entries(key);
        //判断商品是否存在  存在就修改商品信息  如id  数量  当前价格
        if (map.containsKey(goodsId.toString())){
            temp = (CartVo) map.get(goodsId.toString());
            temp.setGoodsNum(temp.getGoodsNum() + cartVo.getGoodsNum());
            temp.setMarketPrice(cartVo.getMarketPrice());

        }
        //当不存在的情况  直接添加新商品
        hashOperations.put(key,goodsId.toString(),temp);
        System.out.println(temp);
        return BaseResult.success();
    }

    /**
     * 商品更新
     * @param cartVo
     * @param userId
     * @return
     */
    @Override
    public BaseResult updateCartVo(CartVo cartVo, Integer userId) {
        /**
         * 1.参数校验
         *    商品记录存在校验
         *    数量>0
         * 2.更新商品数量
         *     根据商品id 更新value
         * 3.返回结果
         */
        //获取商品id
        Integer goodsId = cartVo.getGoodsId();
        if (null == goodsId || 0 == goodsId) {
            return BaseResult.error("商品记录不存在!");
        }
        GoodsExample example = new GoodsExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<Goods> goodsList = goodsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(goodsList)) {
            return BaseResult.error("商品记录不存在!");
        }
        cartVo.setAddTime(new Date());
        // 购物车key
        String key = userCart + ":" + userId;
        hashOperations.put(key,goodsId.toString(),cartVo);
        return BaseResult.success();

    }

    /**
     * 删除商品
     * @param goodsId
     * @param userId
     * @return
     */
    @Override
    public BaseResult delCartVo(Integer goodsId, Integer userId) {
        if (null == goodsId || 0 == goodsId) {
            return BaseResult.error("商品记录不存在!");
        }
        GoodsExample example = new GoodsExample();
        example.createCriteria().andGoodsIdEqualTo(goodsId);
        List<Goods> goodsList = goodsMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(goodsList)) {
            return BaseResult.error("商品记录不存在!");
        }
        String key = userCart + ":" + userId;
        //删除map
        hashOperations.delete(key,goodsId.toString());
        return BaseResult.success();
    }

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    @Override
    public BaseResult clearCart(Integer userId) {
        //删除redis
        redisTemplate.delete(userCart + ":" + userId);
        return BaseResult.success();
    }

    /**
     * 统计商品数量
     * @param userId
     * @return
     */
    @Override
    public Integer countGoodsNum(Integer userId) {
        return hashOperations.values(userCart + ":" + userId)
                .stream().mapToInt(c->{
                    CartVo cartVo = (CartVo) c;
                    return cartVo.getGoodsNum();
                }).sum();
    }

    /**
     * 获取商品列表 统计总金额和总重量
     * @param userId
     * @return
     */
    @Override
    public CartResult getCartListInfo(Integer userId) {
        CartResult result =new CartResult();
        String key = userCart + ":" + userId;
        if(redisTemplate.hasKey(key)){
            List<CartVo> cartVoList=new ArrayList<CartVo>();
            Map<String,Object> map= hashOperations.entries(key);
            BigDecimal total=BigDecimal.ZERO;
            for(Map.Entry<String,Object> entry:map.entrySet()){
                CartVo cartVo = (CartVo) entry.getValue();
                total=total.add(cartVo.getMarketPrice().multiply(BigDecimal.valueOf(cartVo.getGoodsNum())));
                cartVoList.add(cartVo);
            }
            // 商品列表
            result.setCartList(cartVoList);
            // 商品总金额
            result.setTotalPrice(total);
            // 商品总件数
            result.setCount(this.countGoodsNum(userId));
        }
        return result;
    }
}
