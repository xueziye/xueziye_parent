package cn.xzy.core.service;

import cn.xzy.core.pojo.item.Item;
import cn.xzy.core.util.Constants;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public Map<String, Object> search(Map paramMap) {

        Map<String, Object> map = highLightSearch(paramMap);
        //2根据查询的参数到solr中获取对应的分类结果 因为分类有重复
        List<String> groupCategroup = findGroupCategroup(paramMap);
        map.put("categoryList",groupCategroup);
        //3 判断paranMap传入的参数中是否有分类的名称
        String category = String.valueOf(paramMap.get("category"));

        if(category!=null&&!"".equals(category)){
            //5 如果有分类参数 则根据分布可以查询对应的品牌集合
            Map specListAndBrandList = findSpecListAndBrandList(category);
            map.putAll(specListAndBrandList);
        }else{
            //4 如果没有  根据第一个分类查询对应的商品集合
            Map specListAndBrandList = findSpecListAndBrandList(groupCategroup.get(0));
            map.putAll(specListAndBrandList);
        }

        return map;
    }
    //4 根据分类名称查询对象品牌集合和规格的集合
    private Map findSpecListAndBrandList(String categoryName){
        //a.根据分类名称到 redis中查询对相应的模板id
        Long templateId =(Long) redisTemplate.boundHashOps(Constants.CATEGORY_LIST_REDIS).get(categoryName);
        //b.根据模板id去redis中去查询对应的品牌集合
        List<Map> brandList = (List<Map>)redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS).get(templateId);
        //c.根据模板id去redis去查询对应的规格集合
        List<Map> specList =(List<Map>) redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).get(templateId);
        //d.将品牌集合和规格集合封装到Map集合中  返回
        Map resultMap = new HashMap<>();
        resultMap.put("brandList",brandList);
        resultMap.put("specList",specList);
        return resultMap;
    }

    //2根据查询的参数到solr中获取对应的分类结果 因为分类有重复
    private List<String> findGroupCategroup(Map paramMap){
        List<String> resultList = new ArrayList<>();
        //获取关键字
        String keywords = String.valueOf(paramMap.get("keywords"));
        //创建查询对象
        SimpleQuery query = new SimpleQuery();
        //创建查询条件对象
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        //将查询条件放入到查询对象中
        query.addCriteria(criteria);
        //创建分组对象
        GroupOptions groupOptions = new GroupOptions();
        //设置根据分类域进行分组
        groupOptions.addGroupByField("item_category");
        //将分组对象放入到查询对象中
        query.setGroupOptions(groupOptions);
        //使用分组查询   分类集合
        GroupPage<Item> items = solrTemplate.queryForGroupPage(query, Item.class);
        //获得结果集合中的分类域集合
        GroupResult<Item> item_category = items.getGroupResult("item_category");
        //获得分类域中的实体集合
        Page<GroupEntry<Item>> groupEntries = item_category.getGroupEntries();
        //遍历实体集合 得到实体对象
        for (GroupEntry<Item> groupEntry : groupEntries) {
            //组装到集合中然后返回
            String groupValue = groupEntry.getGroupValue();
            resultList.add(groupValue);
        }
        return resultList;

    }
    //1、根据参数关键字，到solr中查询(分页，总条数，总页数),最终版
    private Map<String,Object> highLightSearch(Map paramMap){

        //获取关键字
        String keywords = String.valueOf(paramMap.get("keywords"));
        //获取当前页
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        //每页查询多少条
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));
        //封装查询对象
        HighlightQuery query=new SimpleHighlightQuery();
        //查询的条件对象
        //判断是否选择了品牌
        if(!"".equals(paramMap.get("brand"))){
            Criteria criteria = new Criteria("item_brand").is(paramMap.get("brand"));
            FilterQuery filterQuery=new SimpleFilterQuery(criteria);
            query.addFilterQuery(filterQuery);
        }
        //分类
        if(!"".equals(paramMap.get("category"))){
            Criteria criteria = new Criteria("item_category").is(paramMap.get("category"));
            FilterQuery filterQuery=new SimpleFilterQuery(criteria);
            query.addFilterQuery(filterQuery);
        }
        //价格
        if(!"".equals(paramMap.get("price"))){
            String price=(String)paramMap.get("price");
            String[] split = price.split("-");
            if(!split[0].equals("0")){//如果区间起点不等于0
                Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(split[0]);
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }

            if(!split[1].equals("*")){//如果区间终点不等于*
                Criteria filterCriteria=new Criteria("item_price").lessThanEqual(split[1]);
                FilterQuery filterQuery=new SimpleFilterQuery(filterCriteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //规格
        if(paramMap.get("spec")!=null){
            Map<String,String> map=(Map)paramMap.get("spec");
            for(String key:map.keySet()){
                String spec = map.get(key);
                Criteria criteria = new Criteria("item_spec_"+key).is(spec);
                FilterQuery filterQuery=new SimpleFilterQuery(criteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //排序
        String sortValue= (String) paramMap.get("sort");//ASC  DESC
        String sortField= (String) paramMap.get("sortField");//排序字段

        if(sortValue!=null && !sortValue.equals("")){
            if(sortValue.equals("ASC")){
                Sort sort=new Sort(Sort.Direction.ASC, "item_"+sortField);
                query.addSort(sort);
            }
            if(sortValue.equals("DESC")){
                Sort sort=new Sort(Sort.Direction.DESC, "item_"+sortField);
                query.addSort(sort);
            }
        }
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        //将查询条件对象，放入查询对象中
        query.addCriteria(criteria);
        //判断当前页是否为null
        if(pageNo==null||pageNo<=0){
            pageNo=1;
        }
        //开始查询的位置
        Integer start=(pageNo-1)*pageSize;
        //设置从第几条开始查询
        query.setOffset(start);
        //设置每页查询多少条
        query.setRows(pageSize);
        //创建高亮显示对象
        HighlightOptions options = new HighlightOptions();
        //设置哪个域需要高亮显示
        options.addField("item_title");
        //设置高亮前缀
        options.setSimplePrefix("<em style='color:red'>");
        //设置高亮后缀
        options.setSimplePostfix("</em>");
        //将高亮加入到查询对象中
        query.setHighlightOptions(options);
        //查询返回集合
        HighlightPage<Item> items =  solrTemplate.queryForHighlightPage(query, Item.class);
        //获得高亮集合
        List<HighlightEntry<Item>> highlighted = items.getHighlighted();
        List<Item> list = new ArrayList<>();
        //遍历高亮集合
        for (HighlightEntry<Item> highlightEntry : highlighted) {
            Item item = highlightEntry.getEntity();
            List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
            if(highlights!=null){
                //获取高亮的标题集合
                List<String> highlightTitle = highlights.get(0).getSnipplets();
                //判断
                if(highlightTitle!=null&&highlightTitle.size()>0){
                    //获取高亮标题
                    String title = highlightTitle.get(0);
                    item.setTitle(title);
                }
            }
            list.add(item);
        }
        Map<String, Object> resultMap = new HashMap<>();
        //查询到的结果集
        resultMap.put("rows",list);
        //总页数
        resultMap.put("totalPages",items.getTotalPages());
        //总条数
        resultMap.put("total",items.getTotalElements());
        return resultMap;
    }
}
