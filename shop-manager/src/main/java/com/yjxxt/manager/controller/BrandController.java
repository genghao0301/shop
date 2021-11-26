package com.yjxxt.manager.controller;

import com.yjxxt.manager.pojo.Brand;
import com.yjxxt.manager.service.IBrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Resource
    private IBrandService brandService;

    /**
     * 查询品牌列表
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public List<Brand> queryBrands(){
        return brandService.queryBrands();
    }
}
