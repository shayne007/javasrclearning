package com.feng.es.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 12/2/21
 */
@Data
@Document(indexName = "sku",
        useServerConfiguration = true, createIndex = false)
public class Sku {
    @Id
    private Long sku_id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
}
