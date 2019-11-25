package cn.xzy.manager.controller;

import cn.xzy.core.entity.FastDFSClient;
import cn.xzy.core.entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class Upload {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file){
        try{
            FastDFSClient fastDFS = new FastDFSClient("classpath:fastDFS//fdfs_client.conf");
            String s = fastDFS.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            return  new Result(true,FILE_SERVER_URL+s);
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"上传失败");
        }

    }
}
