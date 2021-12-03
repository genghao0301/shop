package com.yjxxt.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    @RequestMapping("/{page}")
    public String page(@PathVariable String page, String redirectUrl, Model model){
        if(page.equals("login")){
            model.addAttribute("redirectUrl",redirectUrl);
        }
        return page;
    }
}
