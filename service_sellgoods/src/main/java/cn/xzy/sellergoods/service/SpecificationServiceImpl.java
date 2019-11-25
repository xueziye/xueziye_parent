package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.specification.SpecificationDao;
import cn.xzy.core.dao.specification.SpecificationOptionDao;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.entity.SpecResult;
import cn.xzy.core.pojo.specification.Specification;
import cn.xzy.core.pojo.specification.SpecificationOption;
import cn.xzy.core.pojo.specification.SpecificationOptionQuery;
import cn.xzy.core.pojo.specification.SpecificationQuery;
import cn.xzy.core.service.SpecificationService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecificationDao specDao;

    @Autowired
    private SpecificationOptionDao specOptDao;
    @Override
    public PageResult findPage(int pageNum, int pageSize,Specification spec) {
        PageHelper.startPage(pageNum,pageSize);
        SpecificationQuery query=new SpecificationQuery();
        SpecificationQuery.Criteria criteria = query.createCriteria();

            if(spec.getSpecName()!=null&&spec.getSpecName().length()>0){
                criteria.andSpecNameLike("%"+spec.getSpecName()+"%");
            }

        Page<Specification> page=(Page<Specification>)specDao.selectByExample(query);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void add(SpecResult specResult) {
        specDao.insertSelective(specResult.getSpecification());
        List<SpecificationOption> list = specResult.getSpecificationOptionList();
        if(list!=null){
            for (SpecificationOption option : list) {
                option.setSpecId(specResult.getSpecification().getId());
                specOptDao.insert(option);
            }
        }
    }

    @Override
    public SpecResult findOne(Long id) {
        SpecResult specResult=new SpecResult();
        specResult.setSpecification(specDao.selectByPrimaryKey(id));
        SpecificationOptionQuery query=new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(id);
        specResult.setSpecificationOptionList(specOptDao.selectByExample(query));
        return specResult;
    }

    @Override
    public void upate(SpecResult specResult) {
        //先将Specification更新
        specDao.updateByPrimaryKey(specResult.getSpecification());
        //2将SpecificationOption中SpecId为specResult.getSpecification().getId()的删除
        SpecificationOptionQuery query=new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = query.createCriteria();
        criteria.andSpecIdEqualTo(specResult.getSpecification().getId());
        //3将SpecificationOption更新
        List<SpecificationOption> list = specResult.getSpecificationOptionList();
        specOptDao.deleteByExample(query);
        if(list!=null){
            for (SpecificationOption option : list) {
                option.setSpecId(specResult.getSpecification().getId());

                specOptDao.insert(option);
            }
        }
    }

    @Override
    public void delect(Long[] ids) {
        for (Long id : ids) {
            specDao.deleteByPrimaryKey(id);
            SpecificationOptionQuery query=new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecIdEqualTo(id);
            specOptDao.deleteByExample(query);
        }

    }

    @Override
    public List<Map> selectOptionList() {
        return specDao.selectOptionList();
    }
}
