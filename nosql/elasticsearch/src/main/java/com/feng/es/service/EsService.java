package com.feng.es.service;

import com.alibaba.fastjson.JSON;
import com.feng.es.domain.Sku;
import com.feng.es.repository.DataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 12/2/21
 */
@Service
public class EsService {

    @Resource
    private DataRepository dataRepository;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;


    public String query(Long id) {
        Sku sku = dataRepository.findById(id).orElse(null);
        return Objects.toString(sku, null);
    }

    public String findListByCondition(Sku sku, Integer pageIndex, Integer pageSize) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria()
                .and(new Criteria("title").contains(sku.getTitle())))
                .setPageable(PageRequest.of(pageIndex, pageSize));

        SearchHits<Sku> searchHits = elasticsearchRestTemplate.search(criteriaQuery, Sku.class);
        List<Sku> result = searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
        System.out.println("get total size: " + searchHits.getTotalHits());

        return JSON.toJSONString(result);
    }

    public List<Sku> findAll(Integer pageIndex, Integer pageSize) {
        Page<Sku> page = dataRepository.findAll(PageRequest.of(pageIndex, pageSize));

        List<Sku> result = page.getContent();
        System.out.println("get total size: " + page.getTotalElements());
        return result;
    }

}
