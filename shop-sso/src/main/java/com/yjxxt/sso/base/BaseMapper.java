package com.yjxxt.sso.base;


import java.util.List;

public interface BaseMapper<T,R> {
    int insertSelective(T record);

    List<T> selectByExample(R r);

    T selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(T record);

    int deleteByPrimaryKey(Short id);
}
