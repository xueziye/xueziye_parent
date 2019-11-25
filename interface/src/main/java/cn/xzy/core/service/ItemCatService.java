package cn.xzy.core.service;

import cn.xzy.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    List<ItemCat> findByParentId(Long parentId);

    ItemCat findOne(Long id);

    void add(ItemCat cat);

    void update(ItemCat cat);

    void delete(Long[] ids);

    List<ItemCat> findAll();
}
