package com.yjxxt.rpc.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车结果对象
 *
 * 
 * @since 1.0.0
 */
public class CartResult implements Serializable {
   private static final long serialVersionUID = -8936811376383018291L;
   // 购物车列表
   private List<CartVo> cartList;

   // 购物车总金额
   private BigDecimal totalPrice=BigDecimal.ZERO;

   // 商品总数
   private Integer count=0;


   // 商品总重量
   private Integer weight=0;


   public Integer getCount() {
      return count;
   }

   public void setCount(Integer count) {
      this.count = count;
   }

   public Integer getWeight() {
      return weight;
   }

   public void setWeight(Integer weight) {
      this.weight = weight;
   }

   public List<CartVo> getCartList() {
      return cartList;
   }

   public void setCartList(List<CartVo> cartList) {
      this.cartList = cartList;
   }

   public BigDecimal getTotalPrice() {
      return totalPrice;
   }

   public void setTotalPrice(BigDecimal totalPrice) {
      this.totalPrice = totalPrice;
   }

   @Override
   public String toString() {
      return "CartResult{" +
            "cartList=" + cartList +
            ", totalPrice=" + totalPrice +
            '}';
   }
}