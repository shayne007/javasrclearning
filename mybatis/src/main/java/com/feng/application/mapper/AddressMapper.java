package com.feng.application.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.feng.application.domain.Address;

/**
 * Created on 2020-10-29
 */
public interface AddressMapper {
    /**
     * 根据id查询Address对象
     * 
     * @param id
     * @return
     */
    Address find(long id);

    /**
     * 查询一个用户的全部地址信息
     * 
     * @param customerId
     * @return
     */
    List<Address> findAll(long customerId);

    /**
     * 查询指定订单的送货地址
     * 
     * @param orderId
     * @return
     */
    Address findByOrderId(long orderId);

    /**
     * 存储Address对象，同时会记录关联的Customer
     * 
     * @param address
     * @param customerId
     * @return
     */
    int save(@Param("address") Address address, @Param("customerId") long customerId);
}