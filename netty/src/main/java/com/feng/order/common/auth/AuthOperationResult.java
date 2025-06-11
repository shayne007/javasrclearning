package com.feng.order.common.auth;

import com.feng.order.common.OperationResult;

import lombok.Data;

/**
 * @author fengsy
 * @date 8/3/21
 * @Description
 */
@Data
public class AuthOperationResult extends OperationResult {
    private final boolean passAuth;

    public AuthOperationResult(boolean passAuth) {
        this.passAuth = passAuth;
    }

    public AuthOperationResult() {
        this.passAuth = false;
    }

    public boolean isPassAuth() {
        return passAuth;
    }
}
