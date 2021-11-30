package com.yjxxt.portal.controller;

//import com.yjxxt.common.result.ShopPageInfo;
import com.yjxxt.common.result.ShopPageInfo;
import com.yjxxt.rpc.service.ISearchService;
import com.yjxxt.rpc.vo.GoodsVo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController {
    @Reference(version = "1.0")
    private ISearchService  searchService;


    /**
     * 转发到搜索结果页
     * @return
     */
    @RequestMapping("search/index")
    public String index(String searchStr, Model model){
        model.addAttribute("searchStr",searchStr);
        return "doSearch";
    }


    @ResponseBody
    @RequestMapping("search/searchGoods")
    public ShopPageInfo<GoodsVo> search(String searchStr,Integer pageNum,Integer pageSize){
        return searchService.doSearch(searchStr, pageNum, pageSize);
    }
//    @RequestMapping("serach")
//    @ResponseBody
//    public ShopPageInfo<GoodsVo> serach(String searchStr,Integer pageNum,Integer pageSize){
//        ListageI<GoodsVo> goodsVoList= searchService.doSearch(searchStr,pageNum,pageSize);
//        ShopPageInfo<GoodsVo> shopPageInfo=new ShopPageInfo<>(pageNum,pageSize);
//        shopPnfo.setResult(goodsVoList);
//        return shopPageInfo;
//    }


}
