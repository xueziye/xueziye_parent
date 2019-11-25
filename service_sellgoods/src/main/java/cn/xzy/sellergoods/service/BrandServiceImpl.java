package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.good.BrandDao;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.good.Brand;
import cn.xzy.core.pojo.good.BrandQuery;
import cn.xzy.core.service.BrandService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;
    @Override
    public List<Brand> getAll() {
        List<Brand> brands = brandDao.selectByExample(null);
        return brands;
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        Page<Brand> page=(Page<Brand>)brandDao.selectByExample(null);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(Brand brand) {
         brandDao.insert(brand);
    }

    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKey(brand);
    }

    @Override
    public PageResult search(Brand brand, int page, int rows) {
        PageHelper.startPage(page,rows);
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if(brand!=null){
            if(brand.getName()!=null && brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }
        Page<Brand> page1=(Page<Brand>) brandDao.selectByExample(brandQuery);

        return new PageResult(page1.getTotal(),page1.getResult());
    }

    @Override
    public void delect(Long[] id) {
        for (Long aLong : id) {
            brandDao.deleteByPrimaryKey(aLong);
        }
    }

    @Override
    public List<Map> selectOptionList() {
        return brandDao.selectOptionList();
    }
}
















