package com.yjxxt.rpc.service;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.rpc.vo.CartResult;
import com.yjxxt.rpc.vo.CartVo;

/**
 * 购物车服务接口
 */
public interface ICartService {
    /**
     * 添加商品记录到购物车
     * @param cartVo
     * @param userId
     * @return
     */
    public BaseResult saveCartVo(CartVo cartVo,Integer userId);
    /**
     * 更新购物车商品数量
     * @param cartVo
     * @param userId
     * @return
     */
    public BaseResult updateCartVo(CartVo cartVo,Integer userId);


    /**
     * 删除购物车指定商品记录
     * @param goodsId
     * @param userId
     * @return
     */
    public BaseResult delCartVo(Integer goodsId,Integer userId);

    /**
     * 清空购物车
     * @param userId
     * @return
     */
    public BaseResult clearCart(Integer userId);


    /**
     * 统计购物车商品总数
     * @param userId
     * @return
     */
    public Integer countGoodsNum(Integer userId);


    /**
     * 返回购物车列表
     * @param userId
     * @return
     */
    public CartResult getCartListInfo(Integer userId);
}
