package cn.xzy.manager.controller;

import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.item.ItemCat;
import cn.xzy.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId){
        return itemCatService.findByParentId(parentId);
    }

    @RequestMapping("/findOne")
    public ItemCat finOne(Long id){
        return itemCatService.findOne(id);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody ItemCat cat){
        try {
            itemCatService.add(cat);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    @RequestMapping("/update")
    public Result update(@RequestBody ItemCat cat){
        try {
            itemCatService.update(cat);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    @RequestMapping("/findAll")
    public List<ItemCat> findAll(){
        return  itemCatService.findAll();
    }
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try {
            itemCatService.delete(ids);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
}
