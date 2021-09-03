package com.feng.application.service;

import java.math.BigDecimal;
import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import com.feng.application.DaoUtils;
import com.feng.application.domain.Address;
import com.feng.application.domain.Order;
import com.feng.application.domain.OrderItem;
import com.feng.application.mapper.AddressMapper;
import com.feng.application.mapper.OrderItemMapper;
import com.feng.application.mapper.OrderMapper;
import com.google.common.base.Preconditions;

/**
 * Created on 2020-10-30
 */
public class OrderService {

    // 创建订单
    public long createOrder(Order order) {
        Preconditions.checkArgument(order != null, "order is null");
        Preconditions.checkArgument(order.getOrderItems() != null && order.getOrderItems().size() > 0,
            "orderItems is empty");
        return DaoUtils.execute(sqlSession -> {
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
            OrderItemMapper orderItemMapper = sqlSession.getMapper(OrderItemMapper.class);
            // 调用OrderMapper.save()方法完成订单的持久化
            long affected = orderMapper.save(order);
            if (affected <= 0) {
                throw new RuntimeException("Save Order fail...");
            }
            long orderId = order.getId();
            for (OrderItem orderItem : order.getOrderItems()) {
                // 通过OrderItemMapper完成OrderItem的持久化
                orderItemMapper.save(orderItem, orderId);
            }
            return orderId;
        });
    }

    // 根据订单id查询订单的全部信息
    public Order find(long orderId) {
        // 检查orderId参数是否合法
        Preconditions.checkArgument(orderId > 0, "orderId error");
        return DaoUtils.execute(sqlSession -> {
            // 查询该订单关联的全部OrderItem
            OrderItemMapper orderItemMapper = sqlSession.getMapper(OrderItemMapper.class);
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(orderId);
            // 查询订单本身的信息
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
            Order order = orderMapper.find(orderId);
            order.setOrderItems(orderItems);
            // 计算订单总额
            order.setTotalPrice(calculateTotalPrice(order));
            // 查询订单关联的Address
            AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
            Address address = addressMapper.find(order.getDeliveryAddress().getId());
            order.setDeliveryAddress(address);
            return order;
        });
    }

    private BigDecimal calculateTotalPrice(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderItem orderItem : orderItems) {
            BigDecimal itemPrice =
                orderItem.getProduct().getPrice().getAmount().multiply(new BigDecimal(orderItem.getAmount()));
            orderItem.setPrice(Money.of(CurrencyUnit.of("CNY"), itemPrice));
            totalPrice.add(itemPrice);
        }
        return totalPrice;
    }

    public Order findByCustomerId(long customerId) {
        // 检查orderId参数是否合法
        Preconditions.checkArgument(customerId > 0, "customerId error");
        return DaoUtils.execute(sqlSession -> {
            // 查询该订单关联的全部OrderItem
            OrderItemMapper orderItemMapper = sqlSession.getMapper(OrderItemMapper.class);
            List<OrderItem> orderItems = orderItemMapper.findByOrderId(customerId);
            // 查询订单本身的信息
            OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
            List<Order> orderList = orderMapper.findByCustomerId(customerId, 0L, 0L);
            Order order = orderList.get(0);
            order.setOrderItems(orderItems);
            // 计算订单总额
            order.setTotalPrice(calculateTotalPrice(order));
            // 查询订单关联的Address
            AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
            Address address = addressMapper.find(order.getDeliveryAddress().getId());
            order.setDeliveryAddress(address);
            return order;
        });
    }
}