package com.feng.application.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.feng.application.domain.Order;

/**
 * Created on 2020-10-29
 */
public interface OrderMapper {
    // 根据订单Id查询
    Order find(long id);

    // 查询一个用户一段时间段内的订单列表
    List<Order> findByCustomerId(@Param("id") long customerId, @Param("startTime") long startTime,
        @Param("endTime") long endTime);

    // 保存一个订单
    long save(Order order);
}