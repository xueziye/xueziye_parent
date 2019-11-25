package cn.xzy.manager.controller;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.ad.Content;
import cn.xzy.core.pojo.ad.ContentCategory;
import cn.xzy.core.service.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    @Reference
    private ContentService contentService;

    @RequestMapping("/findAll")
    public List<Content> getAll(){
        List<Content> all = contentService.findAll();
        return all;
    }
    @RequestMapping("/findPage")
    public PageResult findPage(int page,int rows){
        return contentService.findPage(page,rows);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Content category){
        try {
            contentService.add(category);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    @RequestMapping("/findOne")
    public Content findOne(Long id){
        return contentService.findOne(id);
    }
    @RequestMapping("/update")
    public Result update(@RequestBody Content category){
        try {
            contentService.update(category);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody Content category, int page , int rows){
        return contentService.search(category,page,rows);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            contentService.delect(ids);
            return new Result(true,"删除成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

}
