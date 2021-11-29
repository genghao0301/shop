package com.yjxxt.rpc.service.impl;

import com.yjxxt.rpc.mapper.GoodsCategoryMapper;
import com.yjxxt.rpc.pojo.GoodsCategory;
import com.yjxxt.rpc.pojo.GoodsCategoryExample;
import com.yjxxt.rpc.service.IGoodsCategoryService;
import com.yjxxt.rpc.vo.GoodsCategoryVo;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service(version = "1.0")
public class GoodsCategoryServiceImpl implements IGoodsCategoryService {

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;


    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOperations;

    @Value("${goods.category.list.key}")
    private String goodsCategoryCacheKey;

    @Override
    public List<GoodsCategoryVo> queryGoodsCategoryVoForListView() {
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
}
