package cn.xzy.core.entity;

import cn.xzy.core.pojo.good.Goods;
import cn.xzy.core.pojo.good.GoodsDesc;
import cn.xzy.core.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

public class GoodsEntity implements Serializable {
    private Goods goods;
    private GoodsDesc goodsDesc;
    private List<Item> itemList;

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public GoodsEntity(Goods goods, GoodsDesc goodsDesc, List<Item> itemList) {
        this.goods = goods;
        this.goodsDesc = goodsDesc;
        this.itemList = itemList;
    }

    public GoodsEntity() {
    }
}
