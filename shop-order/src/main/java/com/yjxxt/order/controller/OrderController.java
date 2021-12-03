package com.yjxxt.order.controller;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.order.service.IOrderService;
import com.yjxxt.rpc.service.ICartService;
import com.yjxxt.sso.pojo.Admin;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {
    @Reference(version = "1.0")
    private ICartService cartService;
    @Resource
    private IOrderService orderService;



    /**
     * 跳转到预订单界面
     * @return
     */
    @RequestMapping("preOrder")
    public String preOrder(HttpSession session, Model model){
        //从session作用域中获取用户信息
        Admin admin = (Admin) session.getAttribute("user");
        //通过用户id查到该用户购物车数据存到model中
        model.addAttribute("cartResult",cartService.getCartListInfo(Integer.parseInt(admin.getAdminId().toString())));
        return "order/preOrder";
    }

    /**
     * 跳转到提交订单界面
     * @return
     */
    @RequestMapping("submitOrder")
    public String submitOrder(HttpSession session,Model model){
        //从session作用域中获取用户信息
        Admin admin = (Admin) session.getAttribute("user");
        //添加订单信息到数据库  批量添加商品到数据库
        BaseResult baseResult = orderService.saveOrder(Integer.parseInt(admin.getAdminId().toString()));
        if(baseResult.getCode()!=200){
            //如果失败就跳转到失败界面 并提示失败信息
            model.addAttribute("msg",baseResult.getMessage());
            return "error";
        }
        //从返回结果中获取订单号和总金额 存到model中方便前端显示
        Map<String,Object> map= (Map<String, Object>) baseResult.getResult();
        model.addAttribute("totalAmount",map.get("totalAmount"));
        model.addAttribute("orderSn",map.get("orderSn"));
        return "order/submitOrder";
    }
}
