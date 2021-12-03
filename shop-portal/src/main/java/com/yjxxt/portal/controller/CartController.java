package com.yjxxt.portal.controller;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.rpc.service.ICartService;
import com.yjxxt.rpc.vo.CartVo;
import com.yjxxt.sso.pojo.Admin;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("cart")
public class CartController {
    //注入购物车接口
    @Reference(version = "1.0")
    private ICartService cartService;

    @Value("${shop.order.url}")
    private String shopOrderUrl;

    /**
     * 购物车主页面
     * @return
     */
    @RequestMapping("index")
    public String index(Model model, HttpSession session){
        //从session中获取用户信息
        Admin admin = (Admin) session.getAttribute("user");
        //将购物车列表存入model 前端遍历
        model.addAttribute("cartResult",cartService.getCartListInfo(Integer.parseInt(admin.getAdminId().toString())));
        return "cart";
    }

    /**
     * 添加商品到购物车
     * @param cartVo
     * @param session
     * @return
     */
    @RequestMapping("saveCartVo")
    @ResponseBody
    public BaseResult saveCartVo(CartVo cartVo, HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");
//        System.out.println(admin);
//        System.out.println(admin.getAdminId());
        return  cartService.saveCartVo(cartVo, Integer.parseInt(admin.getAdminId().toString()));
    }

    @RequestMapping("getCartNum")
    @ResponseBody
    public Integer getCartNum(HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");
        return  cartService.countGoodsNum(Integer.parseInt(admin.getAdminId().toString()));
    }


    /**
     * 删除购物车商品记录
     * @param goodsId
     * @param session
     * @return
     */
    @RequestMapping("delCartVo")
    @ResponseBody
    public BaseResult delCartVo(Integer goodsId,HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");
        return cartService.delCartVo(goodsId,Integer.parseInt(admin.getAdminId().toString()));
    }

    /**
     * 清空购物车商品
     * @param session
     * @return
     */
    @RequestMapping("clearCart")
    @ResponseBody
    public BaseResult clearCart(HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");
        return cartService.clearCart(Integer.parseInt(admin.getAdminId().toString()));
    }


    /**
     * 商品购物车商品数量
     * @param cartVo
     * @param session
     * @return
     */
    @RequestMapping("updateCartVo")
    @ResponseBody
    public BaseResult updateCartVo(CartVo cartVo,HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");
        return cartService.updateCartVo(cartVo,Integer.parseInt(admin.getAdminId().toString()));
    }


    @RequestMapping("toPreOrder")
    public String toPreOrder(){
        return "redirect:"+shopOrderUrl+"/order/preOrder";
    }

}
