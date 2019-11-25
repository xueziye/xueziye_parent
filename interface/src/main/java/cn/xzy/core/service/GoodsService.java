package cn.xzy.core.service;

import cn.xzy.core.entity.GoodsEntity;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.good.Goods;

public interface GoodsService {
    void add(GoodsEntity goodsEntity);

    PageResult search(Goods goods, int page, int rows);

    GoodsEntity findOne(Long id);

    void update(GoodsEntity goodsEntity);

    void delete(Long[] ids);

    void updateStatus(long[] ids, String status);

    void isMarketable(String isStatus,Long[] ids);
}
