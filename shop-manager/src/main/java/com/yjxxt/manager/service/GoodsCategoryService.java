package com.yjxxt.manager.service;

import com.yjxxt.manager.mapper.GoodsCategoryMapper;
import com.yjxxt.manager.pojo.GoodsCategory;
import com.yjxxt.manager.pojo.GoodsCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsCategoryService {
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    public GoodsCategory queryGoodsCategoryById(Short id){
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }
    public GoodsCategory queryGoodsCategoryByGoodsCategoryExample(Short id){
        GoodsCategoryExample example=new GoodsCategoryExample();
        example.createCriteria().andIdEqualTo(id);
        return goodsCategoryMapper.selectByExample(example).get(0);
    }

    /**
     * 根据上级分类id 查询子分类记录
     * @param parentId
     * @return
     */
    public List<GoodsCategory> queryGoodsCategoriesByParentId(Short parentId){
        GoodsCategoryExample example=new GoodsCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
       return goodsCategoryMapper.selectByExample(example);
    }
}
