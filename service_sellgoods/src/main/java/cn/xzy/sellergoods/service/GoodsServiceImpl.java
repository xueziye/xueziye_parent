package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.good.BrandDao;
import cn.xzy.core.dao.good.GoodsDao;
import cn.xzy.core.dao.good.GoodsDescDao;
import cn.xzy.core.dao.item.ItemCatDao;
import cn.xzy.core.dao.item.ItemDao;
import cn.xzy.core.dao.seller.SellerDao;
import cn.xzy.core.entity.GoodsEntity;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.good.Brand;
import cn.xzy.core.pojo.good.Goods;
import cn.xzy.core.pojo.good.GoodsDesc;
import cn.xzy.core.pojo.good.GoodsQuery;
import cn.xzy.core.pojo.item.Item;
import cn.xzy.core.pojo.item.ItemCat;
import cn.xzy.core.pojo.item.ItemQuery;
import cn.xzy.core.pojo.seller.Seller;
import cn.xzy.core.service.GoodsService;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private SellerDao sellerDao;

    @Override
    public PageResult search(Goods goods, int page, int rows) {
        PageHelper.startPage(page,rows);
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        criteria.andIsDeleteIsNull();
        if(goods!=null){
            if(goods.getGoodsName()!=null){
                criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
            }
            if(goods.getAuditStatus()!=null){
                criteria.andAuditStatusEqualTo(goods.getAuditStatus());
            }
            if(goods.getSellerId()!=null){
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }

        }
        Page<Goods> page1=(Page<Goods>)goodsDao.selectByExample(goodsQuery);
        return new PageResult(page1.getTotal(),page1.getResult());
    }

    @Override
    public GoodsEntity findOne(Long id) {
        Goods goods = goodsDao.selectByPrimaryKey(id);
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(itemQuery);
        return new GoodsEntity(goods,goodsDesc,items);
    }

    @Override
    public void update(GoodsEntity goodsEntity) {
        //商品的修改
        goodsDao.updateByPrimaryKey(goodsEntity.getGoods());
        //商品详情的修改
        goodsDescDao.updateByPrimaryKeySelective(goodsEntity.getGoodsDesc());
        //规格修改，考虑到有新添加的规格，所以删除后添加，有一个创建时间的BUG
        ItemQuery query=new ItemQuery();
        ItemQuery.Criteria criteria = query.createCriteria();
        criteria.andGoodsIdEqualTo(goodsEntity.getGoods().getId());
        List<Item> cd = itemDao.selectByExample(query);
        List<Item> cl=goodsEntity.getItemList();
        //先将传来的数据，赋值title，利用title与查到的title相等,错误了。
        for (Item item : cl) {
            String goodsName = goodsEntity.getGoods().getGoodsName();
            String spec = item.getSpec();
            Map parse = JSON.parseObject(spec, Map.class);
            Collection<String> values = parse.values();
            for (String value : values) {
                goodsName+=" "+value;
            }
            item.setTitle(goodsName);
            System.out.println(item.getTitle());
            for (Item item1 : cd) {
                //相等时将创建时间赋值给传来的item
                if(item.getTitle().equals(item1.getTitle())){
                    System.out.println(item.getCreateTime());
                    item.setCreateTime(item1.getCreateTime());
                }
            }
        }
        itemDao.deleteByExample(query);
        insertItem(goodsEntity);
        /*for (Item item : goodsEntity.getItemList()) {
            itemDao.updateByPrimaryKeySelective(item);
        }*/
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            Goods goods = new Goods();
            goods.setId(id);
            goods.setIsDelete("1");
            goodsDao.updateByPrimaryKeySelective(goods);
        }
    }

    @Override
    public void updateStatus(long[] ids, String status) {
        for (long id : ids) {
            Goods goods = new Goods();
            goods.setId(id);
            goods.setAuditStatus(status);
            goodsDao.updateByPrimaryKeySelective(goods);
        }
    }
    //上下架
    @Override
    public void isMarketable(String isStatus,Long[] ids) {
        for (long id : ids) {
            Goods goods = goodsDao.selectByPrimaryKey(id);
            goods.setIsMarketable(isStatus);
            goodsDao.updateByPrimaryKeySelective(goods);
        }

    }

    @Override
    public void add(GoodsEntity goodsEntity) {
        //添加商品
        goodsEntity.getGoods().setAuditStatus("0");
        goodsDao.insertSelective(goodsEntity.getGoods());
        //添加商品详情，商品的id与商品详情的id一致
        Long id = goodsEntity.getGoods().getId();
        goodsEntity.getGoodsDesc().setGoodsId(id);
        goodsDescDao.insert(goodsEntity.getGoodsDesc());
        //保存库存集合对象
        insertItem(goodsEntity);
    }
    private void insertItem(GoodsEntity goodsEntity){
        if("1".equals(goodsEntity.getGoods().getIsEnableSpec())){
            //有库存
            if( goodsEntity.getItemList()!=null){
                for(Item item:goodsEntity.getItemList()){
                    //库存标题由商品名称和规格组成
                    String goodsName = goodsEntity.getGoods().getGoodsName();
                    String spec = item.getSpec();
                    Map parse = JSON.parseObject(spec, Map.class);
                    Collection<String> values = parse.values();
                    for (String value : values) {
                        goodsName+=" "+value;
                    }
                    item.setTitle(goodsName);
                    //设置库存对象的属性
                    setItemValue(goodsEntity, item);
                    itemDao.insertSelective(item);
                }
            }
        }else{
            Item item = new Item();
            item.setPrice(new BigDecimal("99999999"));
            item.setNum(0);
            item.setSpec("");
            item.setTitle(goodsEntity.getGoods().getGoodsName());
            //设置库存对象的属性值
            setItemValue(goodsEntity,item);
            itemDao.insertSelective(item);
        }
    }

    private Item setItemValue(GoodsEntity goodsEntity,Item item){
        //商品的id
        item.setGoodsId(goodsEntity.getGoods().getId());
        if(item.getCreateTime()==null){
            //创建的时间
            item.setCreateTime(new Date());
        }
        //更新的时间
        item.setUpdateTime(new Date());
        //库存状态
        item.setStatus("0");
        //分类的id 库存分类
        item.setCategoryid(goodsEntity.getGoods().getCategory3Id());
        //分类的名称
        ItemCat itemCat=itemCatDao.selectByPrimaryKey(goodsEntity.getGoods().getCategory3Id());
        item.setCategory(itemCat.getName());
        //卖家的名称
        Seller seller = sellerDao.selectByPrimaryKey(goodsEntity.getGoods().getSellerId());
        item.setSeller(seller.getName());
        //品牌的名称
        Brand brand=brandDao.selectByPrimaryKey(goodsEntity.getGoods().getBrandId());
        item.setBrand(brand.getName());
        //实例的图片
        String itemImages = goodsEntity.getGoodsDesc().getItemImages();
        List<Map> maps = JSON.parseArray(itemImages, Map.class);
        if(maps!=null&&maps.size()>0){
            String url =String.valueOf(maps.get(0).get("url"));
            item.setImage(url);
        }
        return item;
    }
}
