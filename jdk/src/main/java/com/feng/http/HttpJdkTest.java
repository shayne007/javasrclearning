package com.feng.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @Description jdk http client demo 使用JDK的 java.net.HttpURLConnection发起HTTP请求
 * @Author fengsy
 * @Date 9/23/21
 */
public class HttpJdkTest {
    public static String jdkGet(String url) {

        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            URL restServiceUrl = new URL(url);
            connection = (HttpURLConnection)restServiceUrl.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("token", "myTokenStr");
            connection.addRequestProperty("Authorization", "myAuthorizationStr");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Failed with error code: " + connection.getResponseCode());
            }

            inputStream = connection.getInputStream();

            byte[] bytes = new byte[1024];
            int length = -1;

            while ((length = inputStream.read(bytes)) != -1) {
                stringBuilder.append(new String(bytes, 0, length));
            }

        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String url = "http://110.42.251.23:8080/views/seckill.html";
        String response = jdkGet(url);
        System.out.println(response);
    }
}
