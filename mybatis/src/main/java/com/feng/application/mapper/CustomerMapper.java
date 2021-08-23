package com.feng.mybatis.mapper;

import com.feng.mybatis.domain.Customer;

/**
 * Created on 2020-10-29
 */
public interface CustomerMapper {
    /**
     * 根据用户Id查询Customer(不查询Address)
     * 
     * @param id
     * @return
     */
    Customer find(long id);

    /**
     * 根据用户Id查询Customer(同时查询Address)
     * 
     * @param id
     * @return
     */
    Customer findWithAddress(long id);

    /**
     * 根据orderId查询Customer
     * 
     * @param orderId
     * @return
     */
    Customer findByOrderId(long orderId);

    /**
     * 持久化Customer对象
     * 
     * @param customer
     * @return
     */
    int save(Customer customer);
}