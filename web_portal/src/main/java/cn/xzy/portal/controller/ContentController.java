package cn.xzy.portal.controller;

import cn.xzy.core.pojo.ad.Content;
import cn.xzy.core.service.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;

    /*@RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryId(Long categoryId){
        return contentService.findByCategoryId(categoryId);
    }*/
    @RequestMapping("/findByCategoryId")
    public List<Content> findByCategoryIdFromRedis(Long categoryId){
        return contentService.findByCategoryIdFromRedis(categoryId);
    }
}

