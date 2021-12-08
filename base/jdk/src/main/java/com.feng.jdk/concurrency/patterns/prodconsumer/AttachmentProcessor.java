package com.feng.concurrency.patterns.prodconsumer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.concurrent.ArrayBlockingQueue;

import com.feng.concurrency.patterns.prodconsumer.resuable.BlockingQueueChannel;
import com.feng.concurrency.patterns.prodconsumer.resuable.Channel;
import com.feng.concurrency.patterns.twophaseterminate.reusable.AbstractTerminatableThread;
import com.feng.concurrency.util.Tools;

/**
 * 模式角色：Producer-Consumer.Producer
 * 
 * @author fengsy
 * @date 5/18/21
 * @Description
 */

public class AttachmentProcessor {
    public static final String ATTACHMENT_STORE_BASE_DIR = Tools.getWorkingDir("./attachments");

    // 模式角色：Producer-Consumer.Channel
    private final Channel<File> channel = new BlockingQueueChannel<File>(new ArrayBlockingQueue<File>(200));

    // 模式角色：Producer-Consumer.Consumer
    private final AbstractTerminatableThread indexingThread = new AbstractTerminatableThread() {

        @Override
        protected void doRun() throws Exception {
            File file = null;

            file = channel.take();
            try {
                indexFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                terminationToken.reservations.decrementAndGet();
            }

        }

        // 根据指定文件生成全文搜索所需的索引文件
        private void indexFile(File file) throws Exception {
            // 省略其他代码

            // 模拟生成索引文件所需的耗时
            Tools.randomPause(100, 80);
        }

    };

    public void init() {
        indexingThread.start();
    }

    public void shutdown() {
        indexingThread.terminate();
    }

    public void saveAttachment(InputStream in, String documentId, String originalFileName) throws IOException {
        // 将附件保存为文件
        File file = saveAsFile(in, documentId, originalFileName);
        try {
            channel.put(file);
            indexingThread.terminationToken.reservations.incrementAndGet();
        } catch (InterruptedException e) {
            ;
        }
    }

    private File saveAsFile(InputStream in, String documentId, String originalFileName) throws IOException {
        String dirName = ATTACHMENT_STORE_BASE_DIR + documentId;
        File dir = new File(dirName);
        dir.mkdirs();
        File file = new File(dirName + '/' + Normalizer.normalize(originalFileName, Normalizer.Form.NFC));

        // 防止目录跨越攻击
        if (!new File(dirName).equals(new File(file.getCanonicalFile().getParent()))) {
            throw new SecurityException("Invalid originalFileName:" + originalFileName);
        }
        try (InputStream dataIn = in) {
            Files.copy(dataIn, Paths.get(file.getCanonicalPath()), StandardCopyOption.REPLACE_EXISTING);
        }
        return file;
    }
}