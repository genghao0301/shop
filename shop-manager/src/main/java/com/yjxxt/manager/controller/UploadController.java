package com.yjxxt.manager.controller;


import com.yjxxt.common.result.FileResult;
import com.yjxxt.manager.service.IUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class UploadController {

    @Resource
    private IUploadService uploadService;


    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping("fileUpload/save")
    @ResponseBody
    public FileResult uploadFile(@RequestParam(name = "file") MultipartFile file){
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
        } catch (IOException e) {
            e.printStackTrace();
            fileResult.setError("文件上传失败!");
        }
        return fileResult;
    }
}
