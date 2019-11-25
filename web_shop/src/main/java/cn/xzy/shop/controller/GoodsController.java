package cn.xzy.shop.controller;

import cn.xzy.core.entity.GoodsEntity;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.good.Goods;
import cn.xzy.core.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Reference
    private GoodsService goodsService;

    @RequestMapping("/add")
    public Result add(@RequestBody GoodsEntity goodsEntity){
        try{
            goodsEntity.getGoods().setSellerId(SecurityContextHolder.getContext().getAuthentication().getName());
            goodsService.add(goodsEntity);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }

    //分页
    @RequestMapping("/search")
    public PageResult search(@RequestBody Goods goods,int page,int rows){
        goods.setSellerId(SecurityContextHolder.getContext().getAuthentication().getName());
        return goodsService.search(goods,page,rows);
    }

    //更新回显
    @RequestMapping("/findOne")
    public GoodsEntity findOne(Long id){
        return goodsService.findOne(id);
    }
    @RequestMapping("/update")
    public Result update(@RequestBody GoodsEntity goodsEntity){
        try{
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            if(!name.equals(goodsEntity.getGoods().getSellerId())){
                return new Result(false,"您没有权限修改此商品！");
            }
            goodsService.update(goodsEntity);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    //上下架
    @RequestMapping("/updateIsMarketable")
    public Result isMarketable(Long[] ids,String isMarketable){
        try{
            goodsService.isMarketable(isMarketable,ids);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
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
}
