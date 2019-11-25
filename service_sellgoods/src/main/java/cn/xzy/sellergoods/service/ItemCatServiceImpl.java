package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.item.ItemCatDao;
import cn.xzy.core.pojo.item.ItemCat;
import cn.xzy.core.pojo.item.ItemCatQuery;
import cn.xzy.core.service.ItemCatService;
import cn.xzy.core.util.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Transactional
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao catDao;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //获取所有分类数据
        List<ItemCat> itemCats = catDao.selectByExample(null);
        //分类名称作为key  模板的id作为value
        for (ItemCat itemCat : itemCats) {
            redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).put(itemCat.getName(),itemCat.getTypeId());
        }
        //根据父级的id查询它的子集 展示到页面
        ItemCatQuery query=new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        return catDao.selectByExample(query);
    }

    @Override
    public ItemCat findOne(Long id) {
        return catDao.selectByPrimaryKey(id);
    }

    @Override
    public void add(ItemCat cat) {
        catDao.insertSelective(cat);
    }

    @Override
    public void update(ItemCat cat) {
        catDao.updateByPrimaryKeySelective(cat);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            catDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<ItemCat> findAll() {
        return catDao.selectByExample(null);
    }
}
