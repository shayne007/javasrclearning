package com.feng.springcore.prototype;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeService {}