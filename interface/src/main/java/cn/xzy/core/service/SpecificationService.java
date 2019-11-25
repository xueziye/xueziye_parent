package cn.xzy.core.service;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.SpecResult;
import cn.xzy.core.pojo.specification.Specification;

import java.util.List;
import java.util.Map;

public interface SpecificationService {
    PageResult findPage(int pageNum, int pageSize, Specification spec);

    void add(SpecResult specResult);

    SpecResult findOne(Long id);

    void upate(SpecResult specResult);

    void delect(Long[] ids);

    List<Map> selectOptionList();
}
