package com.feng.springcore.lifecycle;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import com.feng.springcore.lifecycle.disposable.ClosableLightService;

/**
 * @author fengsy
 * @date 7/8/21
 * @Description
 */
@Component
public class LightMgrService implements InitializingBean {

    /**
     * 使用 @Autowired 直接标记在成员属性上而引发的装配行为是发生在构造器执行之后的。
     */
    // @Autowired
    ClosableLightService lightService;

    public LightMgrService(ClosableLightService lightService) {
        this.lightService = lightService;
        lightService.check();
    }

    @PostConstruct
    public void init() {
        lightService.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lightService.check();
    }
}
