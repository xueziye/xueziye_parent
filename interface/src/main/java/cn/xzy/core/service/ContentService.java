package cn.xzy.core.service;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.ad.Content;
import cn.xzy.core.pojo.ad.ContentCategory;

import java.util.List;
import java.util.Map;

public interface ContentService {
    public List<Content> findAll();

    public PageResult findPage(int pageNum, int pageSize);

    void add(Content content);

    Content findOne(Long id);

    void update(Content content);

    PageResult search(Content content, int page, int rows);

    void delect(Long[] id);

    List<Map> selectOptionList();

    List<Content> findByCategoryId(Long categoryId);

    List<Content> findByCategoryIdFromRedis(Long categoryId);
}
