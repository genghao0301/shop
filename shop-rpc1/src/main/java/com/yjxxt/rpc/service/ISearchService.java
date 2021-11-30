package com.yjxxt.rpc.service;

import com.yjxxt.common.result.ShopPageInfo;
import com.yjxxt.rpc.vo.GoodsVo;

import java.util.List;

public interface ISearchService {
    ShopPageInfo<GoodsVo> doSearch(String searchStr, Integer pageNum, Integer pageSize);
}
