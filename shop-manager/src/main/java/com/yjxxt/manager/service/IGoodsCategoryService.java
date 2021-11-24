package com.yjxxt.manager.service;

import com.yjxxt.manager.pojo.GoodsCategory;
import com.yjxxt.manager.vo.GoodsCategoryVo;

import java.util.List;

public interface IGoodsCategoryService {
    GoodsCategory queryGoodsCategoryById(Short id);

    GoodsCategory queryGoodsCategoryByGoodsCategoryExample(Short id);

    List<GoodsCategory> queryGoodsCategoriesByParentId(Short parentId);

    int saveGoodsCategory(GoodsCategory goodsCategory);

    List<GoodsCategoryVo> queryGoodsCategoryVoForListView();
}
