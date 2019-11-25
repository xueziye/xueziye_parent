package cn.xzy.core.service;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.ad.ContentCategory;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    public List<ContentCategory> getAll();

    public PageResult findPage(int pageNum, int pageSize);

    void add(ContentCategory category);

    ContentCategory findOne(Long id);

    void update(ContentCategory category);

    PageResult search(ContentCategory category, int page, int rows);

    void delect(Long[] id);

    List<Map> selectOptionList();
}
