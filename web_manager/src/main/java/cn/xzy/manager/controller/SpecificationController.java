package cn.xzy.manager.controller;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.entity.SpecResult;
import cn.xzy.core.pojo.specification.Specification;
import cn.xzy.core.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specification")
public class SpecificationController {
    @Reference
    private SpecificationService specService;

    //分页查询
    @RequestMapping("/search")
    public PageResult findPage(@RequestBody Specification spec,int page, int rows){
        return specService.findPage(page,rows,spec);
    }

    //添加
    @RequestMapping("/add")
    public Result add(@RequestBody SpecResult specResult){

        try{
            specService.add(specResult);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    //根据id返回数据
    @RequestMapping("/findOne")
    public SpecResult findOne(Long id){
        return specService.findOne(id);
    }

    //更新
    @RequestMapping("/update")
    public Result update(@RequestBody SpecResult specResult){
        try{
            specService.upate(specResult);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    //删除
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            specService.delect(ids);
            return new Result(true,"删除成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
    //在模板管理添加时返回的下拉列表
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return specService.selectOptionList();
    }
}
