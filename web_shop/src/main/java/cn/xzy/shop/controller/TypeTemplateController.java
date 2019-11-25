package cn.xzy.shop.controller;

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

    //修改回显
    @RequestMapping("/findOne")
    public TypeTemplate findOne(Long id){
        return templateService.findOne(id);
    }

    //根据模板id 查询规格的集合 和规格选项集合
    @RequestMapping("/findBySpecList")
    public List<Map> findBySpecList(long id){
        return templateService.findBySpecList(id);
    }
}
