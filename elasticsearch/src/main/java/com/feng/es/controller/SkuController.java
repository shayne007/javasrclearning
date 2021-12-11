package com.feng.es.controller;

import com.feng.es.domain.Sku;
import com.feng.es.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 12/2/21
 */
@RestController
public class SkuController {
    @Autowired
    EsService service;

    @GetMapping(value = "/sku/{id}", produces = "application/json;charset=UTF-8")
    public String queryById(@PathVariable String id) {
        return service.query(Long.valueOf(id));
    }

    @GetMapping(value = "/sku/{title}", produces = "application/json;charset=UTF-8")
    public String queryByCondition(@PathVariable String title) {
        Sku example = new Sku();
        example.setTitle(title);
        return service.findListByCondition(example, 0, 10);
    }

}
