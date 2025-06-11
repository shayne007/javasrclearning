package com.feng.jdk.http;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    private static final String EN_CODED = "UTF-8";

    public static String doPost(String url, String JSONBody, String auth) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");

        if (StringUtils.isNotEmpty(auth)) {
            httpPost.addHeader("Authorization", auth);
            ;
        }

        if (StringUtils.isNotEmpty(JSONBody)) {
            httpPost.setEntity(new StringEntity(JSONBody));
        }
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, EN_CODED);
        response.close();
        httpClient.close();
        return responseContent;
    }

    public static String doGet(String url, Map<String, String> para) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        if (MapUtils.isNotEmpty(para)) {
            for (Map.Entry<String, String> entry : para.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity, EN_CODED);
        response.close();
        httpClient.close();
        return responseContent;
    }

    public static String httpMultipartFormData(Map<String, String> textMap, Map<String, File> fileMap, String url) {
        LOGGER.info("开始http调用地址:" + url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (textMap != null && !textMap.isEmpty()) {
            Iterator<String> it = textMap.keySet().iterator();
            while (it.hasNext()) {
                String textName = (String) it.next();
                builder.addTextBody(textName, (String) textMap.get(textName), ContentType.TEXT_PLAIN);
            }
        }
        Map<String, String> tempFileMap = new HashMap<String, String>();
        // 把文件加到HTTP的post请求中
        if (fileMap != null && !fileMap.isEmpty()) {
            Iterator<String> it = fileMap.keySet().iterator();
            while (it.hasNext()) {
                String inputName = (String) it.next();
                File file = (File) fileMap.get(inputName);
                try {
                    tempFileMap.put(inputName, file.getPath());
                    builder.addBinaryBody(inputName, new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM,
                            file.getName());
                } catch (FileNotFoundException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        LOGGER.info("请求form text data信息:" + textMap);
        LOGGER.info("请求form file data信息:" + tempFileMap);
        CloseableHttpResponse response = null;
        try {
            LOGGER.info("开始调用:");
            response = httpClient.execute(uploadFile);
            int statusCode = response.getStatusLine().getStatusCode();
            LOGGER.info("返回http响应码:" + statusCode);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        HttpEntity responseEntity = response.getEntity();
        String sResponse = null;
        try {
            sResponse = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (ParseException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("响应信息：" + sResponse);
        return sResponse;
    }

}
