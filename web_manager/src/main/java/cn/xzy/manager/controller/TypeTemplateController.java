package cn.xzy.manager.controller;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.template.TypeTemplate;
import cn.xzy.core.service.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

    @Reference
    private TypeTemplateService templateService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody TypeTemplate template,int page,int rows){
        return templateService.search(template,page,rows);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TypeTemplate template){
        try{
            templateService.add(template);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    //修改回显
    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id){
        return templateService.findOne(id);
    }

    //修改
    @RequestMapping("/update")
    public Result update(@RequestBody TypeTemplate template){
        try{
            templateService.update(template);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try{
            templateService.delete(ids);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return templateService.selectOptionList();
    }
}
