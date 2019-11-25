package cn.xzy.manager.controller;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.good.Brand;
import cn.xzy.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("/getAll")
    public List<Brand> getAll(){
        List<Brand> all = brandService.getAll();
        return all;
    }
    @RequestMapping("/findPage")
    public PageResult findPage(int page,int rows){
        return brandService.findPage(page,rows);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Brand brand){
        try {
            brandService.add(brand);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    @RequestMapping("/findOne")
    public Brand findOne(Long id){
        return brandService.findOne(id);
    }
    @RequestMapping("/update")
    public Result update(@RequestBody Brand brand){
        try {
            brandService.update(brand);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    @RequestMapping("/search")
    public PageResult search(@RequestBody Brand brand,int page ,int rows){
        return brandService.search(brand,page,rows);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            brandService.delect(ids);
            return new Result(true,"删除成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
    //在模板管理添加时返回的下拉列表
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return brandService.selectOptionList();
    }

}
