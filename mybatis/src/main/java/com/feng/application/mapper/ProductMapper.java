package com.feng.application.mapper;

import java.util.List;

import com.feng.application.domain.Product;

/**
 * @author fengsy
 * @date 7/24/21
 * @Description
 */
public interface ProductMapper {

    /**
     * 根据id查询商品信息
     * 
     * @param id
     * @return
     */
    Product find(long id);

    /**
     * 根据名称搜索商品信息
     * 
     * @param name
     * @return
     */
    List<Product> findByName(String name);

    /**
     * 保存商品信息
     * 
     * @param product
     * @return
     */
    long save(Product product);
}
