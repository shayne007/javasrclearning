package com.feng.jdk.concurrency.patterns.stconfinement;

import com.feng.jdk.concurrency.util.Tools;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class SampleClient {
    private static final MessageFileDownloader DOWNLOADER;

    static {

        // 请根据实际情况修改构造器MessageFileDownloader的参数
        MessageFileDownloader mfd = null;
        try {
            mfd = new MessageFileDownloader(Tools.getWorkingDir("ch11"), "192.168.1.105", "ftp", "ftp", "~/messages");
            mfd.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DOWNLOADER = mfd;
    }

    public static void main(String[] args) {
        DOWNLOADER.downloadFile("abc.xml");
        DOWNLOADER.downloadFile("123.xml");
        DOWNLOADER.downloadFile("xyz.xml");

        // 执行其他操作
        Tools.randomPause(30000, 10000);
        DOWNLOADER.shutdown();
    }
}
