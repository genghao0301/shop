package com.yjxxt.portal.controller;

import com.yjxxt.rpc.service.IGoodsCategoryService;
import com.yjxxt.rpc.vo.GoodsCategoryVo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GoodsCategoryController {

    @Reference(version = "1.0")
    private IGoodsCategoryService goodsCategotyService;


    @RequestMapping("goodsCategory/list")
    @ResponseBody
    public List<GoodsCategoryVo> queryGoodsCategories(){
        return goodsCategotyService.queryGoodsCategoryVoForListView();
    }
}
