package com.psi.file.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.file.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;//文件服务器地址

    @PostMapping("/upload")
    public Result upload(@RequestParam(name = "file") MultipartFile file) {
        //获取文件扩展名,不要“.”
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        try {
            //创建一个fastDFSClient
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");
            //上传文件
            String path = fastDFSClient.uploadFile(file.getBytes(), suffix);
            //拼接url
            String url = FILE_SERVER_URL + "/" + path;
            System.out.println(url);
            return new Result(true, StatusCode.OK, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传失败");
        }
    }
}
