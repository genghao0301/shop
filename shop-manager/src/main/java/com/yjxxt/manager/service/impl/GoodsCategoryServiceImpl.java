package com.yjxxt.manager.service.impl;

import com.yjxxt.manager.mapper.GoodsCategoryMapper;
import com.yjxxt.manager.pojo.GoodsCategory;
import com.yjxxt.manager.pojo.GoodsCategoryExample;

import com.yjxxt.manager.service.IGoodsCategoryService;
import com.yjxxt.manager.vo.GoodsCategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsCategoryServiceImpl implements IGoodsCategoryService {
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;


    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOperations;

    @Override
    public GoodsCategory queryGoodsCategoryById(Short id){
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }
    @Override
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
    @Override
    public List<GoodsCategory> queryGoodsCategoriesByParentId(Short parentId){
        GoodsCategoryExample example=new GoodsCategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
       return goodsCategoryMapper.selectByExample(example);
    }

    @Override
    public int saveGoodsCategory(GoodsCategory goodsCategory) {
        return goodsCategoryMapper.insertSelective(goodsCategory);
    }
    /**
     * 查询一级 二级 三级 分类数据
     */
    @Override
    public List<GoodsCategoryVo> queryGoodsCategoryVoForListView(){
        GoodsCategoryExample example=new GoodsCategoryExample();
        example.createCriteria().andParentIdEqualTo((short) 0);
        //先查询一级数据
        List<GoodsCategory> gc01List=goodsCategoryMapper.selectByExample(example);
        List<GoodsCategoryVo> gcv01List=new ArrayList<>();
        //遍历gc01List 将数据存到GoodsCategoryVo
        for (GoodsCategory gc01:gc01List){
            GoodsCategoryVo gcv01=new GoodsCategoryVo();
            //复制 （a,b) 将a复制给b
            BeanUtils.copyProperties(gc01,gcv01);
            //清除原始查询
            example.clear();
            //查询二级分类数据
            example.createCriteria().andParentIdEqualTo(gc01.getId()).andLevelEqualTo((byte)2);
            //获取二级分类数据
            List<GoodsCategory> gc02List=goodsCategoryMapper.selectByExample(example);
            List<GoodsCategoryVo> gcv02List=new ArrayList<>();
            //遍历将数据存到GoodsCategoryVo
            for(GoodsCategory gc02:gc02List){
                GoodsCategoryVo gcv02=new GoodsCategoryVo();
                //复制 （a,b) 将a复制给b
                BeanUtils.copyProperties(gc02,gcv02);
                //清除原始查询
                example.clear();
                //查询三级分类数据
                example.createCriteria().andParentIdEqualTo(gc02.getId()).andLevelEqualTo((byte)3);
                //获取三级分类数据
                List<GoodsCategory> gc03List=goodsCategoryMapper.selectByExample(example);
                List<GoodsCategoryVo> gcv03List=new ArrayList<>();
                for(GoodsCategory gc03:gc03List){
                    GoodsCategoryVo gcv03=new GoodsCategoryVo();
                    //复制 （a,b) 将a复制给b
                    BeanUtils.copyProperties(gc03,gcv03);
                    gcv03List.add(gcv03);
                }
                gcv02.setChildren(gcv03List);
                gcv02List.add(gcv02);
            }
            //设置一级分类的二级分类数据
            gcv01.setChildren(gcv02List);
            gcv01List.add(gcv01);
        }
        return gcv01List;
    }

    /**
     * 从缓存中读取
     * @return
     */
    @Override
    public List<GoodsCategory> queryGoodsCategories() {
        List<GoodsCategory> result=null;
        //1.从缓存查询分类数据
        String cacheKey="goods_category::list";
        if(redisTemplate.hasKey(cacheKey)){
            result= (List<GoodsCategory>) valueOperations.get(cacheKey);
            if(!CollectionUtils.isEmpty(result)){
                System.out.println("从缓存读取数据...");
                return result;
            }
        }
        // 2.从数据库查询记录
        result= goodsCategoryMapper.selectByExample(new GoodsCategoryExample());
        if(!CollectionUtils.isEmpty(result)){
            // 3. 数据库存在记录 将记录存入缓存
            valueOperations.set(cacheKey,result);
        }
        System.out.println("从数据库读取数据...");
        // 4.返回数据
        return result;
    }
}
