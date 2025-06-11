package com.feng.redis.util;

import cn.hutool.core.util.StrUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @Description TODO
 * @Author fengsy
 * @Date 11/23/21
 */
public class IOUtil {
    //读jar包根目录下的文件
    public static String loadJarFile(
            ClassLoader loader, String resourceName) {

//        InputStream in = loader.getResourloadJarFileceAsStream(resourceName);
        InputStream in = loader.getResourceAsStream(resourceName);
        if (null == in) {
            return null;
        }
        String out = null;
        try {
            int len = in.available();
            byte[] bytes = new byte[len];

            int readLength = in.read(bytes);
            if ((long) readLength < len) {
                throw new IOException(StrUtil.format("File length is [{}] but read [{}]!", new Object[]{len, readLength}));
            }
            out = new String(bytes, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly(in);
        }
        return out;
    }

    public static void closeQuietly(java.io.Closeable o) {
        if (null == o) {
            return;
        }
        try {
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
