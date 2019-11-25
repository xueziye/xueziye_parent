package cn.xzy.core.service;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    public List<Brand> getAll();

    public PageResult findPage(int pageNum,int pageSize);

    void add(Brand brand);

    Brand findOne(Long id);

    void update(Brand brand);

    PageResult search(Brand brand, int page, int rows);

    void delect(Long[] id);

    List<Map> selectOptionList();
}
