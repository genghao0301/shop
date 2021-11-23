package com.yjxxt.manager.controller;

import com.yjxxt.manager.pojo.GoodsCategory;
import com.yjxxt.manager.service.GoodsCategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("goods_category")
public class GoodsCategoryController {
    @Resource
    private GoodsCategoryService goodsCategoryService;
    @GetMapping("/{id}")
    @ResponseBody
    public GoodsCategory queryGoodsCategoryById(@PathVariable Short id){
        return goodsCategoryService.queryGoodsCategoryById(id);
    }
    @GetMapping("/example/{id}")
    @ResponseBody
    public GoodsCategory queryGoodsCategoryByGoodsCategoryExample(@PathVariable Short id){
        return goodsCategoryService.queryGoodsCategoryByGoodsCategoryExample(id);
    }

}

