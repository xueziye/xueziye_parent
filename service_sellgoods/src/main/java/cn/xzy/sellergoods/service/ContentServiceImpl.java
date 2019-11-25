package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.ad.ContentCategoryDao;
import cn.xzy.core.dao.ad.ContentDao;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.ad.Content;
import cn.xzy.core.pojo.ad.ContentCategory;
import cn.xzy.core.pojo.ad.ContentQuery;
import cn.xzy.core.service.ContentService;
import cn.xzy.core.util.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {
    @Autowired
    private ContentDao contentDao;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<Content> findAll() {
        return contentDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        ContentQuery contentQuery = new ContentQuery();
        ContentQuery.Criteria criteria = contentQuery.createCriteria();
        criteria.andStatusEqualTo("1");
        Page<Content> page=(Page<Content>)contentDao.selectByExample(contentQuery);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Content content) {
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
        contentDao.insertSelective(content);
    }

    @Override
    public Content findOne(Long id) {
        return contentDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Content content) {
        //1.根据广告的id 到mysql数据库中 查询原来的广告对象
        Content content1 = contentDao.selectByPrimaryKey(content.getId());
        //2.根据原来广告对象中的分类id到redis数据库中删除对应的广告集合
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content1.getCategoryId());
        //3.根据新的广告对象的分类id删除redis数据库中的广告集合
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
        //4.将新的广告对象更新到mysql数据库中
        contentDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public PageResult search(Content content, int page, int rows) {
        PageHelper.startPage(page,rows);
        ContentQuery contentQuery = new ContentQuery();
        ContentQuery.Criteria criteria = contentQuery.createCriteria();
        criteria.andStatusEqualTo("1");
        if(content!=null){
            if(content.getCategoryId()!=null){
                criteria.andCategoryIdEqualTo(content.getCategoryId());
            }
        }
        Page<Content> page1=(Page<Content>)contentDao.selectByExample(contentQuery);
        return new PageResult(page1.getTotal(),page1.getResult());
    }

    @Override
    public void delect(Long[] id) {
        Content content = new Content();
        content.setStatus("0");
        for (Long aLong : id) {
            //将redis数据库中对应的广告对象删除
            Content content1 = contentDao.selectByPrimaryKey(aLong);
            redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content1.getCategoryId());
            content.setId(aLong);
            contentDao.updateByPrimaryKeySelective(content);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return null;
    }

    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        ContentQuery contentQuery = new ContentQuery();
        ContentQuery.Criteria criteria = contentQuery.createCriteria();
        criteria.andStatusEqualTo("1");
        criteria.andCategoryIdEqualTo(categoryId);
        return contentDao.selectByExample(contentQuery);
    }

    @Override
    public List<Content> findByCategoryIdFromRedis(Long categoryId) {
        //1、根据分类的id到redis中取数据
        List<Content> list=(List<Content>)redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).get(categoryId);
        //2.如果redis中没有数据，到数据库中取
        if(list==null){
            list=findByCategoryId(categoryId);
            //3、数据库中获取到数据，将数据存入redis中一份
            redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).put(categoryId,list);
        }
        return list;
    }
}
