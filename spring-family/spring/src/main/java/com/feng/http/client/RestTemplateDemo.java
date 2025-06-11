package com.feng.http.client;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author fengsy
 * @date 7/13/21
 * @Description
 */
public class RestTemplateDemo {
    public static void main(String[] args) {

        RestTemplate template = new RestTemplate();

        // testRestTemplate(template);

        // testRestTemplate2(template);
        testRestTemplate3(template);
    }

    private static void testRestTemplate(RestTemplate template) {
        String url = "http://localhost:8080/hi";
        // Map<String, Object> paramMap = new HashMap<String, Object>();
        // JSONObject paramMap = new JSONObject();
        MultiValueMap paramMap = new LinkedMultiValueMap();
        paramMap.add("para1", "001");
        paramMap.add("para2", "002");
        // paramMap.put("para1", "001");
        // paramMap.put("para2", "002");

        String result = template.postForEntity(url, paramMap, String.class).getBody();
        System.out.println(result);
    }

    private static void testRestTemplate2(RestTemplate template) {
        String url = "http://localhost:8080/hi2?para=1#2";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        URI uri = builder.build().encode().toUri();
        HttpEntity<?> entity = new HttpEntity<>(null);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());
    }

    private static void testRestTemplate3(RestTemplate template) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/hi2");
        builder.queryParam("para", "开发测试 001");
        URI url = builder.encode().build().toUri();
        ResponseEntity<String> forEntity = template.getForEntity(url, String.class);
        System.out.println(forEntity.getBody());
    }
}
