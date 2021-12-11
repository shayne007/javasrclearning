package com.feng.jdk.concurrency.patterns.stconfinement;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description 实现文件下载， 模式角色：SerialThreadConfinement.Serializer
 */
public class MessageFileDownloader {
    private final WorkerThread workerThread;

    public MessageFileDownloader(String outputDir, final String ftpServer, final String userName, final String password,
                                 final String servWorkingDir) throws Exception {
        Path path = Paths.get(outputDir);
        if (!path.toFile().exists()) {
            Files.createDirectories(path);
        }
        // workerThread = new WorkerThread(outputDir, ftpServer, userName,
        // password, servWorkingDir);
        workerThread = new FakeWorkerThread(outputDir, ftpServer, userName, password, servWorkingDir);
    }

    public void init() {
        workerThread.start();
    }

    public void shutdown() {
        workerThread.terminate();
    }

    public void downloadFile(String file) {
        workerThread.download(file);
    }

}
