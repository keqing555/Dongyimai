package com.psi.file.controller;

import com.psi.entity.Result;
import com.psi.entity.StatusCode;
import com.psi.file.util.FastDFSClient;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
            FastDFSClient fastDFSClient = new FastDFSClient("./fdfs_client.conf");
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

    @PostMapping("/upload2")
    public Result upload2(@RequestParam(name = "file") MultipartFile file) {
        try {
            File tempFile = new File("D:/Upload/" + file.getOriginalFilename());
            file.transferTo(tempFile);

            //加载配置文件
            ClientGlobal.init("./fdfs_client.conf");
            //创建trackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //创建连接，获取trackerServer对象
            TrackerServer trackerServer = trackerClient.getConnection();
            //创建StorageServer的引用
            StorageServer storageServer = null;
            //创建StorageClient对象，需要参数：TrackerServer对象和StorageServer的引用
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
            //使用StorageClient上传jpg文件
            //   String[] file = storageClient.upload_file("D:/Upload/1f5075a2-1f32-4d34-880a-2b4779a9dd5e.jpg", "jpg", null);
            //String[] path = storageClient.upload_file("D:/Upload/35a367b4-3d0c-480d-bf28-49e2a5389a80雷电将军PV.mp4", "mp4", null);
            //获取文件扩展名,不要“.”
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //返回数组，包括组名和图片的路径
            String[] path = storageClient.upload_file(tempFile.getPath(), suffix, null);
            String url = "http://192.168.128.128:8080";
            for (String str : path) {
                url += "/" + str;
            }
            System.out.println(url);
            tempFile.delete();
            return new Result(true, StatusCode.OK, "上传成功", url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传异常");
        }
    }
}
