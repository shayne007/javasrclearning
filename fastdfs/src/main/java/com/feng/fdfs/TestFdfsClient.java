package com.feng.fdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 12/1/21
 */
public class TestFdfsClient {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("fastdfs.connect_timeout_in_seconds", 15000);
        properties.put("fastdfs.network_timeout_in_seconds", 15000);
        properties.put("fastdfs.tracker_servers", "10.0.4.17:22122");
        properties.put("fastdfs.http.http_tracker_http_port", "8080");
        try {
            ClientGlobal.initByProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        getUrl("content", ".txt");
    }

    public static String getUrl(String imageStr, String fileExtentionName) {
        String url = "";
        TrackerServer trackerServer = null;
        byte[] bytes = null;
        try {
            bytes = imageStr.getBytes(Charset.forName("UTF-8"));
            TrackerClient tracker = new TrackerClient();
            trackerServer = tracker.getConnection();

            StorageClient storageClient = new StorageClient(trackerServer, null);
            String[] fileIds = storageClient.upload_file(bytes, fileExtentionName, null);
            if (fileIds != null) {
                String prefix = "http://110.42.251.23:8888";
                url = prefix + "/" + fileIds[0] + "/" + fileIds[1];
            } else {//可能有网络波动，导致丢包，从而导致返回的结果是空的，这个时候重试一次，减少出错概率
                tracker = new TrackerClient();
                trackerServer = tracker.getConnection();
                storageClient = new StorageClient(trackerServer, null);
                String[] fileIds1 = storageClient.upload_file(bytes, fileExtentionName, null);
                if (fileIds1 != null) {
                    String prefix = "http://110.42.251.23:8888";
                    url = prefix + "/" + fileIds1[0] + "/" + fileIds1[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (trackerServer != null) {
                try {
                    trackerServer.close();
                } catch (IOException e1) {
                }
            }
        }
        return url;
    }
}
