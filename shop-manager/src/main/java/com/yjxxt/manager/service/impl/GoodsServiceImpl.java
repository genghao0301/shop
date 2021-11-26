package com.yjxxt.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjxxt.common.result.BaseResult;
import com.yjxxt.manager.mapper.GoodsMapper;
import com.yjxxt.manager.pojo.Goods;
import com.yjxxt.manager.pojo.GoodsExample;
import com.yjxxt.manager.query.GoodsQuery;
import com.yjxxt.manager.service.IGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl  implements IGoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;
    @Resource(name = "redisTemplate")
    public ValueOperations<String,Object> valueOperations;
    @Override
    public Integer saveGoods(Goods goods) {
        /**
         * 1.参数合法校验
         *     商品名非空
         *     商品类别非空
         *     品牌id 非空
         *     库存数量>0
         *     价格(市场价  成本价本店价) >0
         *     sku & spu 非空
         * 2.商品货号生成
         *
         * 3.默认值设置
         *    更新时间
         *    是否热卖
         *    是否新品
         *    是否实物
         *    是否上架
         *    上架时间
         *    是否推荐
         * 4.执行添加  判断结果
         */
        //校验参数
        checkParams(goods.getSku(),goods.getSpu(),
                goods.getGoodsName(),goods.getCatId(),goods.getBrandId());
        //设置商品货号
        goods.setGoodsSn(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+System.currentTimeMillis());
        goods.setIsHot((byte) 0);
        goods.setIsNew((byte)1);
        goods.setIsReal((byte) 1);
        goods.setIsOnSale((byte) 1);
        //goods.setOnTime();
        goods.setIsRecommend((byte) 0);

        int result = goodsMapper.insertSelective(goods);
        if(result<=0){
            throw  new RuntimeException("商品记录添加失败!");
        }

        // 删除key
        redisTemplate.delete(redisTemplate.keys("goods:list*"));

        // 返回主键
        return goods.getGoodsId();
    }
    //校验工具类，非空 否则抛异常
    private void checkParams(String sku, String spu, String goodsName,
                             Integer catId, Short brandId) {
        if (StringUtils.isBlank(goodsName)){
            throw new RuntimeException("商品名不能为空！");
        }
        if (StringUtils.isBlank(spu)){
            throw new RuntimeException("商品spu不能为空！");
        }
        if (StringUtils.isBlank(sku)){
            throw new RuntimeException("商品sku不能为空！");
        }
        if (null == catId || catId == 0) {
            throw  new RuntimeException("请选择商品类别!");

        }
        if (null == brandId || brandId == 0) {
            throw  new RuntimeException("请选择商品品牌!");
        }
    }

    /**
     * 商品多条件分页查询
     * @param goodsQuery
     * @return
     */
    @Override
    public BaseResult queryGoodsByParams(GoodsQuery goodsQuery) {
        StringBuffer cacheKeyBuffer=new StringBuffer("goods:list:pageNum:"+
                goodsQuery.getPageNum()+":pageSize:"+goodsQuery.getPageSize());
        //创建分页 传入需要创几页，每页多少
        PageHelper.startPage(goodsQuery.getPageNum(),goodsQuery.getPageSize());
        //创建查询条件对象
        GoodsExample example=new GoodsExample();
        //创建
        GoodsExample.Criteria criteria= example.createCriteria();
        //判断参数
        if (null !=goodsQuery.getCatId() ){
            criteria.andCatIdEqualTo(goodsQuery.getCatId());
            cacheKeyBuffer.append(":catId:"+goodsQuery.getCatId());
        }
        if(null !=goodsQuery.getBrandId()){
            criteria.andBrandIdEqualTo(goodsQuery.getBrandId());
            cacheKeyBuffer.append(":brandId:"+goodsQuery.getBrandId());
        }
        if(null !=goodsQuery.getIsNew()){
            criteria.andIsNewEqualTo(goodsQuery.getIsNew());
            //等于1 是新品
            if(1==goodsQuery.getIsNew()){
                criteria.andIsNewEqualTo((byte)1);
                cacheKeyBuffer.append(":isNew:"+goodsQuery.getIsNew());
            }
            //等于2是判断推荐
            if(2==goodsQuery.getIsNew()){
                criteria.andIsRecommendEqualTo((byte)1);
                cacheKeyBuffer.append(":isRecommend:"+goodsQuery.getIsNew());
            }
        }
        if(StringUtils.isNotBlank(goodsQuery.getGoodsName())){
            criteria.andGoodsNameLike("%"+goodsQuery.getGoodsName()+"%");
            cacheKeyBuffer.append(":goodsName:"+goodsQuery.getGoodsName());
        }
        if(null != goodsQuery.getIsOnSale()){
            criteria.andIsOnSaleEqualTo(goodsQuery.getIsOnSale());
            cacheKeyBuffer.append(":isOnSale:"+goodsQuery.getIsOnSale());
        }
        // 1.从缓存中查询商品记录
        //转换成string
        String cacheKey  =  cacheKeyBuffer.toString();
        //判断缓存中是否有这个key
        if(redisTemplate.hasKey(cacheKey)){
            //获取分页数据
            PageInfo<Goods> pageInfo = (PageInfo<Goods>) valueOperations.get(cacheKey);
            if(pageInfo.getTotal()>0){
                return BaseResult.success(pageInfo);
            }
        }
//        //存查询到的Goods
//        List<Goods> goodsList=goodsMapper.selectByExample(example);
//        PageInfo<Goods> pageInfo=new PageInfo<>(goodsList);
//        //先校验商品
//        return BaseResult.success(pageInfo);
        // 2.缓存没有就从数据库查询
        PageHelper.startPage(goodsQuery.getPageNum(),goodsQuery.getPageSize());
        List<Goods> goodsList = goodsMapper.selectByExample(example);
        PageInfo<Goods> pageInfo =new PageInfo<Goods>(goodsList);
        if(pageInfo.getTotal()>0){
            //3.将数据存入缓存
            valueOperations.set(cacheKey,pageInfo);
        }
        // 4.返回缓存数据
        return BaseResult.success(pageInfo);
    }
}
