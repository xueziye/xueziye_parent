package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.specification.SpecificationDao;
import cn.xzy.core.dao.specification.SpecificationOptionDao;
import cn.xzy.core.dao.template.TypeTemplateDao;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.specification.Specification;
import cn.xzy.core.pojo.specification.SpecificationOption;
import cn.xzy.core.pojo.specification.SpecificationOptionQuery;
import cn.xzy.core.pojo.specification.SpecificationQuery;
import cn.xzy.core.pojo.template.TypeTemplate;
import cn.xzy.core.pojo.template.TypeTemplateQuery;
import cn.xzy.core.service.TypeTemplateService;
import cn.xzy.core.util.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TypeTemplateServiceImpl  implements TypeTemplateService {
    @Autowired
    private TypeTemplateDao templateDao;

    @Autowired
    private SpecificationOptionDao sDao;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public PageResult search(TypeTemplate template, int page, int rows) {
        List<TypeTemplate> typeTemplates = templateDao.selectByExample(null);
        //redis中缓存模板的所有数据

        //模板的id作为key 模板的品牌集合作为value
        for (TypeTemplate type : typeTemplates) {
            String brandIdsJsonStr = type.getBrandIds();
            //将json转为集合
            List<Map> brandList = JSON.parseArray(brandIdsJsonStr, Map.class);

            redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS)
                    .put(type.getId(),brandList);
            //将模板id作为键 规格集合作为value
            //System.out.println(type.getId());
            List<Map> specList = findBySpecList(type.getId());
            //List<Map> specList = JSON.parseArray(type.getSpecIds(), Map.class);

            redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS)
                    .put(type.getId(),specList);
        }
        PageHelper.startPage(page,rows);
        TypeTemplateQuery query=new TypeTemplateQuery();
        TypeTemplateQuery.Criteria criteria = query.createCriteria();
        if(template!=null){
            if(template.getName()!=null&&template.getName().length()>0){
                criteria.andNameLike("%"+template.getName()+"%");
            }
        }
        Page<TypeTemplate> page1=(Page<TypeTemplate>)templateDao.selectByExample(query);
        return new PageResult(page1.getTotal(),page1.getResult());
    }

    @Override
    public void add(TypeTemplate template) {
        templateDao.insert(template);
    }

    @Override
    public void update(TypeTemplate template) {
        templateDao.updateByPrimaryKey(template);
    }

    @Override
    public TypeTemplate findOne(Long id) {
        return templateDao.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            templateDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectOptionList() {

        return templateDao.selectOptionList();
    }
    /*@Override
    public List<Map> findBySpecList(long id) {
        TypeTemplate typeTemplate = templateDao.selectByPrimaryKey(id);
        List<Map> list = JSON.parseArray(typeTemplate.getSpecIds(),Map.class);
        for(Map map : list){
            SpecificationOptionQuery query = new SpecificationOptionQuery();
            SpecificationOptionQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecIdEqualTo(new Long((Integer)map.get("id")));
            List<SpecificationOption> options = sDao.selectByExample(query);
            map.put("options",options);
        }
        return list;
    }*/
    @Override
    public List<Map> findBySpecList(long id) {
        //1.根据模板的id 查询模板对象
        TypeTemplate typeTemplate = templateDao.selectByPrimaryKey(id);
        //2.从模板对象中，获取规格数据   获得是json数据
        String specIds = typeTemplate.getSpecIds();
        System.out.println(specIds);
        //3.将json转List集合对象
        List<Map> maps = JSON.parseArray(specIds, Map.class);
        //4.遍历List集合
        if(maps!=null){
            //6.将规格选项数据 在封装到规格选项中 一起返回
            for (Map map : maps) {
                //5.遍历 根据规格的id查询对应的规格选项数据
                Long id1 = Long.parseLong(String.valueOf(map.get("id")));
                //Long id1 =(Long) map.get("id");
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = query.createCriteria();
                //根据规格id获得选项数据
                criteria.andSpecIdEqualTo(id1);
                List<SpecificationOption> l = sDao.selectByExample(query);
                //将选项集合封装到map中
                map.put("options",l);
            }
        }

        return maps;
    }
}
