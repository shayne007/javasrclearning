package com.feng.order.common.order;

import java.util.concurrent.TimeUnit;

import com.feng.order.common.Operation;
import com.google.common.util.concurrent.Uninterruptibles;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Data
@Slf4j
public class OrderOperation extends Operation {
    private int tableId;
    private String dish;

    public OrderOperation(int tableId, String dish) {
        this.tableId = tableId;
        this.dish = dish;
    }

    @Override
    public OrderOperationResult execute() {
        log.info("order's executing startup with orderRequest: " + toString());
        // execute order logic
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        log.info("order's executing complete");
        OrderOperationResult orderResponse = new OrderOperationResult(tableId, dish, true);
        return orderResponse;
    }
}
