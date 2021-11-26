package com.yjxxt.manager.service.impl;

import com.yjxxt.manager.mapper.GoodsImagesMapper;
import com.yjxxt.manager.pojo.GoodsImages;
import com.yjxxt.manager.service.IGoodsImagesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class GoodsImagesServiceImpl implements IGoodsImagesService {
    @Resource
    private GoodsImagesMapper goodsImagesMapper;
    @Override
    public void saveGoodsImages(GoodsImages goodsImages) {
        //检查商品图片id和url是否为空
        checkParams(goodsImages.getGoodsId(),goodsImages.getImageUrl());
        //添加到数据库 属性有图片id 商品id  图片url
        int result = goodsImagesMapper.insertSelective(goodsImages);
        //判断添加返回结果 返回行数小于等于0就是失败
        if(result<=0){
            throw  new RuntimeException("记录添加失败!");
        }
    }

    /**
     * 检查图片id和url
     * @param goodsId
     * @param imageUrl
     */
    private void checkParams(Integer goodsId, String imageUrl) {
        if(null==goodsId || goodsId==0){
            throw  new RuntimeException("商品记录不存在!");
        }
        if(StringUtils.isBlank(imageUrl)){
            throw  new RuntimeException("请指定商品相册地址!");
        }
    }
}
