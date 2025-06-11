package com.feng.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 1/14/22
 */
public class HdfsClient {
    public static void main(String[] args) {
        downloadFileFromHdfs();
        uploadLocalFileToHdfs();
    }

    private static void downloadFileFromHdfs() {
        try {
            String srcFile = "hdfs://122.51.241.109:9000/data/hdfs01.mp4";
            Configuration conf = new Configuration();

            FileSystem fs = FileSystem.get(URI.create(srcFile), conf);
            FSDataInputStream hdfsInStream = fs.open(new Path(srcFile));

            String storePath = "/my/local/store/";
            Path hdfsPath = new Path(storePath);
            if (fs.mkdirs(hdfsPath)) {
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(storePath + "hdfs01" +
                        ".mp4"));
                IOUtils.copyBytes(hdfsInStream, outputStream, 4096, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void uploadLocalFileToHdfs() {
        String source = "/my/local/store/aa.mp4";
        String destination = "hdfs://122.51.241.109:9000/data/hdfs01.mp4";
        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(source));
            //HDFS读写的配置文件
            Configuration conf = new Configuration();
            //生成一个文件系统对象
            FileSystem fs = FileSystem.get(URI.create(destination), conf);
            //生成一个输出流
            OutputStream out = fs.create(new Path(destination));
            IOUtils.copyBytes(in, out, 4096, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
