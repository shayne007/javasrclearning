package com.feng.jvm;

import java.net.URL;

/**
 * @author fengsy
 * @date 1/31/21
 * @Description
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        // jdk 8
        // printBootstrapPaths();
        // System.out.println(System.getProperty("sun.boot.class.path"));
        // System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));

        // 获得加载ClassLoaderTest.class这个类的类加载器
        ClassLoader loader = ClassLoaderTest.class.getClassLoader();
        while (loader != null) {
            System.out.println(loader);
            // 获得父类加载器的引用
            loader = loader.getParent();
        }
        System.out.println(loader);
    }

    private static void printBootstrapPaths() {
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].toExternalForm());
        }
    }
}
