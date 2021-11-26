package com.yjxxt.manager.controller;

import com.yjxxt.common.result.BaseResult;
import com.yjxxt.common.result.FileResult;
import com.yjxxt.manager.pojo.Goods;
import com.yjxxt.manager.pojo.GoodsImages;
import com.yjxxt.manager.query.GoodsQuery;
import com.yjxxt.manager.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("goods")
public class GoodsController {
    @Resource
    private IGoodsCategoryService goodsCategoryService;
    @Resource
    private IBrandService brandService;
    @Resource
    private IGoodsService goodsService;
    @Resource
    private IUploadService uploadService;
    @Resource
    private IGoodsImagesService goodsImagesService;

    /**
     * 跳转到商品列表
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("gcList",goodsCategoryService.queryGoodsCategories());
        model.addAttribute("brandList",brandService.queryBrands());
        return "goods/goods_list";
    }

    /**
     * 新增商品页
     * @return
     */
    @RequestMapping("add/page")
    public String addGoodsPage(Model model){
        model.addAttribute("gcList",
                goodsCategoryService.queryGoodsCategoriesByParentId((short) 0));
        model.addAttribute("brandList",brandService.queryBrands());
        return "goods/goods_add";
    }
    /**
     * 商品添加
     * @param goods
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public BaseResult saveGoods(Goods goods){
        try {
//             goodsService.saveGoods(goods);
//            return BaseResult.success();
            int goodsId = goodsService.saveGoods(goods);
            BaseResult baseResult =BaseResult.success();
            baseResult.setResult(goodsId);
            return baseResult;
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResult.error(e.getMessage());
        }
    }

    /**
     * 保存商品相册
     * @return
     */
    @RequestMapping("images/save")
    @ResponseBody
    public FileResult saveGoodsImages(@RequestParam(name = "file") MultipartFile file,
                                      Integer goodsId){
        FileResult fileResult =new FileResult();
        if(file.isEmpty()){
            fileResult.setError("文件内容不能为空!");
            return fileResult;
        }
        try {
            // 对文件名进行重命名处理  文件名不可重复
            String fileName=file.getOriginalFilename();
            fileName= new SimpleDateFormat("yyyy/MM/dd").format(new Date())
                    +"/"+System.currentTimeMillis()
                    +fileName.substring(fileName.lastIndexOf("."));
            fileResult = uploadService.uploadFile(file.getInputStream(),fileName);
            goodsImagesService.saveGoodsImages(new GoodsImages( goodsId,fileResult.getFileUrl()));
        } catch (Exception e) {
            e.printStackTrace();
            fileResult.setError(e.getMessage());
        }
        return fileResult;
    }

    /**
     * 多条件分页查询
     * @param goodsQuery
     * @return
     */
    @RequestMapping("queryGoodsByParams")
    @ResponseBody
    public BaseResult queryGoodsByParams(GoodsQuery goodsQuery){
        return goodsService.queryGoodsByParams(goodsQuery);
    }

}
