package com.yjxxt.manager.controller;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.manager.pojo.GoodsCategory;
import com.yjxxt.manager.service.GoodsCategoryService;
import com.yjxxt.manager.vo.GoodsCategoryVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("goods_category")
public class GoodsCategoryController {
    @Resource
    private GoodsCategoryService goodsCategoryService;

    /**
     * 商品分类列表
     *
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("gcvList",goodsCategoryService.queryGoodsCategoryVoForListView());
        return "goods/goodsCategory/goods_category_list";
    }

    /**
     * 添加商品
     * @return
     */
    @RequestMapping("addGoodsCategoryPage")
    public String toAddGoodsCategoryPage(Model model){
        //将查询结果存到model中变量为gclist与前端对应
        model.addAttribute("gcList",goodsCategoryService.queryGoodsCategoriesByParentId((short) 0));
        return "goods/goodsCategory/goods_category_add";
    }
    @RequestMapping("queryGoodsCategoriesByParentId")
    @ResponseBody
    public List<GoodsCategory> queryGoodsCategoriesByParentId(Short parentId){
        return goodsCategoryService.queryGoodsCategoriesByParentId(parentId);
    }

    /**
     * 保存商品分类数据
     * @param goodsCategory
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public BaseResult saveGoodsCategory(GoodsCategory goodsCategory){
        int result= goodsCategoryService.saveGoodsCategory(goodsCategory);
        return  result>0?BaseResult.success():BaseResult.error();
    }
    @RequestMapping("queryGoodsCategoryVoForListView")
    @ResponseBody
    public List<GoodsCategoryVo> queryGoodsCategoryVoForListView(){
        return goodsCategoryService.queryGoodsCategoryVoForListView();
    }
}

