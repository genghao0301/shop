package com.yjxxt.manager.controller;


import com.yjxxt.common.enums.BaseResultEnum;
import com.yjxxt.common.result.BaseResult;
import com.yjxxt.common.result.CookieUtil;
import com.yjxxt.manager.service.ICookieService;
import com.yjxxt.sso.service.ISSOService;
import org.apache.commons.lang3.StringUtils;
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
    public BaseResult login(String userName, String password,String verify,
                            HttpServletRequest request, HttpServletResponse response){
        try {

            //获取验证码
            String capText = (String) request.getSession().getAttribute("pictureVerifyKey");
            BaseResult baseResult = new BaseResult();
            //验证码是否一致，不一致返回提示信息
            if (null==verify|| null ==capText || StringUtils.isEmpty(verify.trim()) || !verify.trim().equals(capText)) {
                baseResult.setCode(BaseResultEnum.PASS_ERROR_03.getCode());
                baseResult.setMessage(BaseResultEnum.PASS_ERROR_03.getMessage());
                return baseResult;
            }
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

}
