package com.feng.application;

import java.util.List;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;

import com.feng.application.domain.Address;
import com.feng.application.domain.Customer;
import com.feng.application.domain.Order;
import com.feng.application.domain.OrderItem;
import com.feng.application.domain.Product;
import com.feng.application.service.CustomerService;
import com.feng.application.service.OrderService;
import com.feng.application.service.ProductService;
import com.google.common.collect.Lists;

/**
 * @author fengsy
 * @date 7/24/21
 * @Description
 */
public class CustomerServiceTest {
    private static CustomerService customerService;

    private static OrderService orderService;

    private static ProductService productService;

    @Before
    public void init() {
        customerService = new CustomerService();
        orderService = new OrderService();
        productService = new ProductService();
    }

    @Test
    public void test01() {
        // 创建一个用户
        long customerId = customerService.register("杨四正", "12345654321");
        // 为用户添加一个配送地址
        long addressId = customerService.addAddress(customerId, "牛栏村", "牛栏市", "矮人国");
        System.out.println(addressId);
        // 查询用户信息以及地址信息
        Customer customer = customerService.find(customerId);
        System.out.println(customer);
        Customer customer2 = customerService.findWithAddress(customerId);
        System.out.println(customer2);
        List<Address> addressList = customerService.findAllAddress(customerId);
        addressList.stream().forEach(System.out::println);

        // 入库一些商品
        Product product = new Product();
        product.setName("MyBatis课程");
        product.setDescription("深入MyBatis源码的视频教程");
        product.setPrice(Money.of(CurrencyUnit.of("CNY"), 99.00));
        long productId = productService.createProduct(product);
        System.out.println("create productId:" + product.getId());

        System.out.println(productService.find(product.getId()));

        // 创建一个订单
        Order order = new Order();
        order.setCustomer(customer); // 买家
        order.setDeliveryAddress(addressList.get(0)); // 配送地址
        // 生成购买条目
        OrderItem orderItem = new OrderItem();
        orderItem.setAmount(20);
        orderItem.setProduct(product);
        order.setOrderItems(Lists.newArrayList(orderItem));
        long orderId = orderService.createOrder(order);
        System.out.println("create orderId:" + orderId);

        Order order2 = orderService.find(orderId);
        orderService.findByCustomerId(customer.getId());

        System.out.println(order2);
    }
}
