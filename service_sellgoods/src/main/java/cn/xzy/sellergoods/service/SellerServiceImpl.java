package cn.xzy.sellergoods.service;

import cn.xzy.core.dao.seller.SellerDao;
import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.seller.Seller;
import cn.xzy.core.pojo.seller.SellerQuery;
import cn.xzy.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao sellerDao;

    @Override
    public void add(Seller seller) {
        sellerDao.insert(seller);
    }

    @Override
    public PageResult search(Seller seller, int page, int rows) {
        PageHelper.startPage(page,rows);
        //System.out.println(seller.getStatus());
        SellerQuery query=new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();
        //criteria.andStatusEqualTo("0");
        if(seller!=null){
            String status = seller.getStatus();
            if (status != null && !"".equals(status)){
                criteria.andStatusEqualTo(status);
            }
            /*if(seller.getStatus()!=null&&!"".equals(seller.getStatus())){
                criteria.andStatusEqualTo(seller.getStatus());
            }*/
            if(seller.getName()!=null&&!"".equals(seller.getName())){
                criteria.andNameLike("%"+seller.getName()+"%");
            }
            if(seller.getNickName()!=null&&!"".equals(seller.getNickName())){
                criteria.andNameLike("%"+seller.getNickName()+"%");
            }
        }
        Page<Seller> page1=(Page<Seller>)sellerDao.selectByExample(query);

        return new PageResult(page1.getTotal(),page1.getResult());
    }


    @Override
    public void updateStatus(String sellerId, String  status) {
        Seller seller=new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }

    @Override
    public Seller findOne(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId);
    }
}
