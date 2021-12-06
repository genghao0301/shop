package com.yjxxt.order.controller;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.common.result.CookieUtil;
import com.yjxxt.order.service.IOrderService;
import com.yjxxt.order.service.IPayService;
import com.yjxxt.rpc.service.ICartService;
import com.yjxxt.sso.pojo.Admin;
import com.yjxxt.sso.service.ISSOService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {
    @Reference(version = "1.0")
    private ICartService cartService;
    @Resource
    private IOrderService orderService;
    @Resource
    private IPayService payService;

    @Reference(version = "1.0")
    private ISSOService ssoService;



    /**
     * 跳转到预订单界面
     * @return
     */
    @RequestMapping("preOrder")
    public String preOrder(HttpSession session, Model model,HttpServletRequest request){
        String ticket= CookieUtil.getCookieValue(request,"userTicket");
        //从session作用域中获取用户信息
        Admin admin = ssoService.validateTicket(ticket);
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
    @RequestMapping("payment")
    public String payment(String orderSn ,Model model){
        String result= payService.startPayRequest(orderSn);
        if(StringUtils.isBlank(result)){
            return "error";
        }
        model.addAttribute("result",result);
        return "order/payment";
    }

    /**
     * 异步回调
     */
    @RequestMapping("notifyCallback")
    public String notifyCallback(@RequestParam(name = "out_trade_no") String outTradeNo,
                               @RequestParam(name = "total_amount") BigDecimal totalAmount,
                               @RequestParam(name = "trade_no") String tradeNo,
                               @RequestParam(name = "trade_status") String tradeStatus,
                               HttpServletRequest request){
        BaseResult baseResult = orderService.notifyCallback(outTradeNo,totalAmount,tradeNo,tradeStatus,request);
        request.setAttribute("result","success");
        if(baseResult.getCode() !=200){
            request.setAttribute("result","failed");
        }
        return "order/notify_result";
    }

    /**
     * 同步回调
     */
    @RequestMapping("returnCallback")
    public String returnCallback(@RequestParam(name = "out_trade_no") String outTradeNo,
                                 @RequestParam(name = "total_amount") BigDecimal totalAmount,
                                 @RequestParam(name = "trade_no") String tradeNo,
                                 HttpServletRequest request){

        System.out.println("支付同步回调地址....");
        BaseResult baseResult= orderService.returnCallback(outTradeNo,totalAmount,tradeNo,request);
        request.setAttribute("result",baseResult);
        //重定向  防止用户刷新浏览器地址栏 订单状态重复更新
        return "redirect:test";
    }
    @RequestMapping("test")
    public String test(){
        return "order/pay_result";
    }
}
