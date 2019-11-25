package cn.xzy.core.service;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.template.TypeTemplate;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {
    PageResult search(TypeTemplate template,int page,int rows);

    void add(TypeTemplate template);

    void update(TypeTemplate template);

    TypeTemplate findOne(Long id);

    void delete(Long[] ids);

    List<Map> selectOptionList();

    List<Map> findBySpecList(long id);
}
