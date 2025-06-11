package com.feng.es.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feng.es.domain.Sku;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * Created by memory_fu on 2019/12/26.
 */
@Slf4j
public class ElasticsearchUtil {

    @Value("${origin.es.datasource.host}")
    private String esHost;
    @Value("${archive.es.datasource.port}")
    private String esPort;
    @Value("${archive.es.datasource.clusterName}")
    private String esCluster;
    @Value("${archive.es.datasource.username}")
    String username;

    private String auth;

    @PostConstruct
    private void initailList() {
        String[] user = username.split(",");
        String userName = user[0];
        String userPath = user[1];
        auth = "Basic " + new String(
                Base64.getEncoder().encode((userName + ":" + userPath).getBytes()));
    }

    /**
     * 批量插入档案封面数据
     */
    public int batchInsert(List<Sku> datas) throws IOException {

        if (datas == null || datas.size() == 0) {
            return 0;
        }

        JSONObject indexJsonObject = new JSONObject();
        indexJsonObject.put("_index", "sku");
        indexJsonObject.put("_type", "_doc");

        StringBuilder stringBuilder = new StringBuilder();
        for (Sku sku : datas) {
            JSONObject jsonObject = new JSONObject();
            JSONObject fromObject = JSON.parseObject(JSONObject.toJSONString(sku));
            fromObject.remove("combineCluster");
            indexJsonObject.put("_id", sku.getSku_id());
            jsonObject.put("index", indexJsonObject);

            stringBuilder.append(jsonObject.toString());
            stringBuilder.append("\n");
            stringBuilder.append(fromObject.toString());
            stringBuilder.append("\n");
        }

        String url = "http://" + esHost + ":" + esPort + "/_bulk?pretty";

        String response = HttpClientUtil.doPost(url, stringBuilder.toString(), auth);

        JSONObject responeObject = JSONObject.parseObject(response);
        JSONArray items = responeObject.getJSONArray("items");

        return items == null ? 0 : items.size();
    }

    /**
     * 根据条件更新，目前只支持terms
     */
    public int updateByQuery(String fieldName, List<Object> fieldValues, String updateField,
                             String updateValue, String index) throws IOException {
        JSONObject jsonObject = createBoolQuery(fieldName, fieldValues);

        JSONObject scriptObject = new JSONObject();
        scriptObject.put("source", "ctx._source." + updateField + " = params." + updateField);

        JSONObject paramsObject = new JSONObject();
        paramsObject.put(updateField, updateValue);

        scriptObject.put("params", paramsObject);
        jsonObject.put("script", scriptObject);

        String url =
                "http://" + esHost + ":" + esPort + "/" + index + "/_update_by_query?pretty";

        String response = HttpClientUtil.doPost(url, jsonObject.toString(), auth);
        JSONObject responseObject = JSONObject.parseObject(response);
        int updatedCount = responseObject.getInteger("updated");

        return updatedCount;
    }

    /**
     * 根据条件删除，目前只支持terms
     */
    public int deleteByQuery(String fieldName, List<Object> fieldValues) throws IOException {

        JSONObject queryObject = createBoolQuery(fieldName, fieldValues);
        String url =
                "http://" + esHost + ":" + esPort + "/archive_title_result/_delete_by_query?pretty";

        String response = HttpClientUtil.doPost(url, queryObject.toString(), auth);

        JSONObject jsonObject = JSONObject.parseObject(response);
        int deletedCount = jsonObject.getInteger("deleted");

        return deletedCount;
    }

    /**
     * 创建查询对象，目前只支持terms查询
     */
    private JSONObject createBoolQuery(String fieldName, List<Object> fieldValues) {
        JSONObject jsonObject = new JSONObject();
        JSONObject queryObject = new JSONObject();

        JSONObject boolObject = new JSONObject();
        JSONArray mustArray = new JSONArray();
        JSONObject mustObject = new JSONObject();

        JSONObject termsObject = new JSONObject();
        termsObject.put(fieldName, fieldValues);

        mustObject.put("terms", termsObject);
        mustArray.add(mustObject);
        boolObject.put("must", mustArray);
        queryObject.put("bool", boolObject);
        jsonObject.put("query", queryObject);

        return jsonObject;
    }


}
