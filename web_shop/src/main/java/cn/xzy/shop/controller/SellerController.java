package cn.xzy.shop.controller;

import cn.xzy.core.entity.Result;
import cn.xzy.core.pojo.seller.Seller;
import cn.xzy.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    @RequestMapping("/add")
    public Result add(@RequestBody Seller seller){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(seller.getPassword());
        seller.setPassword(password);
        seller.setStatus("0");
        try {
            sellerService.add(seller);
            return new Result(true,"添加成功");
        }catch (Exception ex){
            ex.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
}
