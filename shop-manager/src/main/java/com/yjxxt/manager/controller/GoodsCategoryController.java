package com.yjxxt.manager.controller;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.manager.pojo.GoodsCategory;
import com.yjxxt.manager.service.IGoodsCategoryService;
import com.yjxxt.manager.vo.GoodsCategoryVo;
import org.apache.dubbo.config.annotation.Reference;
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
    private IGoodsCategoryService goodsCategoryService;
    @Reference(version = "1.0")
    private com.yjxxt.rpc.service.IGoodsCategoryService goodsCategoryService1;

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

    /**
     * 根据上级Id查询 子类分类记录
     * @param parentId
     * @return
     */
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
    @RequestMapping("test")
    @ResponseBody
    public List<com.yjxxt.rpc.vo.GoodsCategoryVo> queryGoodsCategoryVoForListViewByRpc(){
        return goodsCategoryService1.queryGoodsCategoryVoForListView();
    }


}

