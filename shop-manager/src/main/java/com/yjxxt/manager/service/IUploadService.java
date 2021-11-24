package com.yjxxt.manager.service;

import com.yjxxt.common.result.FileResult;

import java.io.InputStream;

public interface IUploadService {

    /**
     * 文件上传接口方法
     * @param is   文件流
     * @param fileName  文件名
     * @return
     */
    public FileResult uploadFile(InputStream is ,String fileName);
}
