package com.yjxxt.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转页面
 *
 *
 * @since 1.0.0
 */
@Controller
public class PageController {
   /**
    * 页面跳转
    * @param page
    * @return
    */
   @RequestMapping("/{page}")
   public String page(@PathVariable String page) {


      return page;
   }
}