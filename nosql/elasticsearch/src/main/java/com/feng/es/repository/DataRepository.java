package com.feng.es.repository;

import com.feng.es.domain.Sku;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 12/2/21
 */
@Repository
public interface DataRepository extends ElasticsearchRepository<Sku, Long> {
}
