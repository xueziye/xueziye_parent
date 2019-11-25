package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.ad.ContentCategoryDao;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.ad.ContentCategory;
import cn.xzy.core.pojo.ad.ContentCategoryQuery;
import cn.xzy.core.service.CategoryService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ContentCategoryDao categoryDao;
    @Override
    public List<ContentCategory> getAll() {
        return categoryDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<ContentCategory> page=(Page<ContentCategory>)categoryDao.selectByExample(null);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(ContentCategory category) {
        categoryDao.insertSelective(category);
    }

    @Override
    public ContentCategory findOne(Long id) {
        return categoryDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(ContentCategory category) {
        categoryDao.updateByPrimaryKeySelective(category);
    }

    @Override
    public PageResult search(ContentCategory category, int page, int rows) {
        PageHelper.startPage(page,rows);
        ContentCategoryQuery contentCategoryQuery = new ContentCategoryQuery();
        Page<ContentCategory> page1=(Page<ContentCategory>)categoryDao.selectByExample(null);
        return new PageResult(page1.getTotal(),page1.getResult());
    }

    @Override
    public void delect(Long[] id) {
        for (Long aLong : id) {
            categoryDao.deleteByPrimaryKey(aLong);
        }

    }

    @Override
    public List<Map> selectOptionList() {
        return null;
    }
}
