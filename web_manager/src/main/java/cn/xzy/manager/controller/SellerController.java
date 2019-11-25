package cn.xzy.manager.controller;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.seller.Seller;
import cn.xzy.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference
    private SellerService sellerService;

    @RequestMapping("/search")
    public PageResult search(@RequestBody Seller seller, int page , int rows){
        //System.out.println(seller.toString());
        return sellerService.search(seller,page,rows);
    }

    //审核
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId,String status){

        try {
            sellerService.updateStatus(sellerId,status);
            return new Result(true,"更新成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    //审核回显
    @RequestMapping("/findOne")
    public Seller findOne(String id){
        return  sellerService.findOne(id);
    }
}
