package cn.xzy.core.service;

import cn.xzy.core.entity.PageResult;
import cn.xzy.core.pojo.seller.Seller;
public interface SellerService {
    void add(Seller seller);

    PageResult search(Seller seller, int page, int rows);

    void updateStatus(String sellerId, String status);

    Seller findOne(String sellerId);
}
