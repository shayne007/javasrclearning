package com.feng.jdk.concurrency.patterns.stconfinement;

import com.feng.jdk.concurrency.util.Debug;
import com.feng.jdk.concurrency.util.Tools;
import org.apache.commons.net.ftp.FTPClient;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class FakeWorkerThread extends WorkerThread {
    public FakeWorkerThread(String outputDir, String ftpServer, String userName, String password, String servWorkingDir)
            throws Exception {
        super(outputDir, ftpServer, userName, password, servWorkingDir);
    }

    @Override
    protected FTPClient initFTPClient(String ftpServer, String userName, String password) throws Exception {
        FTPClient ftpClient = new FTPClient();
        return ftpClient;
    }

    @Override
    protected void doRun() throws Exception {
        String file = workQueue.take();
        try {
            Debug.info("Download file %s", file);
            Tools.randomPause(80, 50);
        } finally {
            terminationToken.reservations.decrementAndGet();
        }

    }
}
