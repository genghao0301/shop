package com.yjxxt.manager.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yjxxt.common.result.FileResult;
import com.yjxxt.manager.service.IUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class UploadServiceImpl implements IUploadService {


    @Value("${qiniu.ak}")
    private String ak;

    @Value("${qiniu.sk}")
    private String sk;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.domain}")
    private String domain;

    @Override
    public FileResult uploadFile(InputStream is, String fileName) {
        FileResult fileResult =new FileResult();
        try {
            //构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Zone.zone2());
            //...其他参数参考类注释
            UploadManager uploadManager = new UploadManager(cfg);
            //默认不指定key的情况下，以文件内容的hash值作为文件名
            Auth auth = Auth.create(ak, sk);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(is, fileName, upToken,null,null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            // 设置文件远程访问地址
            fileResult.setFileUrl(domain+putRet.key);
            fileResult.setSuccess("文件上传成功!");
            fileResult.setMessage("文件上传");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.toString());
            fileResult.setError("文件上传失败!");
        }
        return fileResult;
    }
}
