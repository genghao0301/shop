package com.yjxxt.manager.service.impl;

import com.yjxxt.manager.mapper.BrandMapper;
import com.yjxxt.manager.pojo.Brand;
import com.yjxxt.manager.pojo.BrandExample;
import com.yjxxt.manager.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BrandServiceImpl implements IBrandService {
    @Autowired
    private BrandMapper brandMapper;
    @Override
    public List<Brand> queryBrands() {
        return brandMapper.selectByExample(new BrandExample());
    }
}
