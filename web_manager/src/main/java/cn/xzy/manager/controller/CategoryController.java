package cn.xzy.manager.controller;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.ad.ContentCategory;
import cn.xzy.core.pojo.good.Brand;
import cn.xzy.core.service.CategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contentCategory")
public class CategoryController {
    @Reference
    private CategoryService categoryService;

    @RequestMapping("/findAll")
    public List<ContentCategory> getAll(){
        List<ContentCategory> all = categoryService.getAll();
        return all;
    }
    @RequestMapping("/findPage")
    public PageResult findPage(int page,int rows){
        return categoryService.findPage(page,rows);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody ContentCategory category){
        try {
            categoryService.add(category);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    @RequestMapping("/findOne")
    public ContentCategory findOne(Long id){
        return categoryService.findOne(id);
    }
    @RequestMapping("/update")
    public Result update(@RequestBody ContentCategory category){
        try {
            categoryService.update(category);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody ContentCategory category,int page ,int rows){
        return categoryService.search(category,page,rows);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            categoryService.delect(ids);
            return new Result(true,"删除成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"删除失败");
        }
    }

}
