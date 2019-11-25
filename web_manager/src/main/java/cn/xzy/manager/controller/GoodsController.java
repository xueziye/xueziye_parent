package cn.xzy.manager.controller;

import cn.xzy.core.entity.GoodsEntity;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.good.Goods;
import cn.xzy.core.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Goods goods,int page,int rows){
        return goodsService.search(goods,page,rows);
    }

    @RequestMapping("/findOne")
    public GoodsEntity findOne(Long id){
        return  goodsService.findOne(id);
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try{
            goodsService.delete(ids);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    @RequestMapping("/updateStatus")
    public Result Result(long[] ids,String status){
        try{
            goodsService.updateStatus(ids,status);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

}
