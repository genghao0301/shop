package com.yjxxt.portal.controller;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.common.result.CookieUtil;
//import com.yjxxt.portal.annotations.RequireLogin;
import com.yjxxt.portal.service.ICookieService;
import com.yjxxt.sso.service.ISSOService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("user")
public class UserController {

    @Reference(version = "1.0")
    private ISSOService ssoService;

    @Resource
    private ICookieService cookieService;


    @RequestMapping("login")
    @ResponseBody
    public BaseResult login(String userName, String password,
                            HttpServletRequest request, HttpServletResponse response){
        try {
            String ticket= ssoService.login(userName,password);
            // 将票据信息写入浏览器
            cookieService.setCookie(request,response,ticket);
            return BaseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResult.error(e.getMessage());
        }
    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        String ticket = CookieUtil.getCookieValue(request,"userTicket");
        ssoService.logout(ticket);
        CookieUtil.deleteCookie(request,response,"userTicket");
        return "redirect:"+request.getContextPath()+"/login";
    }

//    /**
//     * 用户必须登录才能访问
//     *    注解标注的方法必须登录
//     *    Aop 拦截@RequireLogin 判断请求中cookie (环绕通知|前置通知)是否包含登录成功的ticket 调用sso.vidateTicket
//     *    校验失败  抛出运行期异常 通过全局异常进行拦截 重定向到登录
//     *    校验成功 方法放行
//     */
//    @RequireLogin
//    public void updateUserImage(){
//        System.out.println("用户头像更新...");
//    }
}
