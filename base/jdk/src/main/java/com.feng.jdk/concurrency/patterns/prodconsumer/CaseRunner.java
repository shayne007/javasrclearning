package com.feng.jdk.concurrency.patterns.prodconsumer;

import com.feng.jdk.concurrency.util.Debug;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author fengsy
 * @date 5/18/21
 * @Description
 */
public class CaseRunner {
    public static void main(String[] args) {
        AttachmentProcessor ap = new AttachmentProcessor();
        ap.init();
        InputStream in = new ByteArrayInputStream("Hello".getBytes());
        try {
            ap.saveAttachment(in, "000887282", "测试文档");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Debug.info("File stored at:\n" + AttachmentProcessor.ATTACHMENT_STORE_BASE_DIR);
    }
}
