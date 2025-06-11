package com.feng.order.common.order;

import com.feng.order.common.OperationResult;

import lombok.Data;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Data
public class OrderOperationResult extends OperationResult {

    private final int tableId;
    private final String dish;
    private final boolean complete;

    public OrderOperationResult(int tableId, String dish, boolean complete) {
        this.tableId = tableId;
        this.dish = dish;
        this.complete = complete;
    }

    public OrderOperationResult() {
        this.tableId = 0;
        this.dish = null;
        this.complete = false;
    }
}
