package com.feng.watcher;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 * @author fengsy
 * @date 4/18/21
 * @Description
 */
public class WatcherTests {
    private ZooKeeper zk;
    private volatile CountDownLatch closeLatch;

    @Before
    public void setUp() throws IOException, InterruptedException {
        DefaultWatcher gw = new DefaultWatcher(this);
        zk = new ZooKeeper("localhost:2181", 5000, gw);
        gw.await();
    }

    public void setCloseLatch(int count) {
        closeLatch = new CountDownLatch(count);
    }

    public void countDownCloseLatch() {
        closeLatch.countDown();
    }

    @Test
    public void testWatchChildren() throws Exception {
        String fatherPath = "/father";
        setCloseLatch(1);

        Stat oneStat = zk.exists(fatherPath, true);
        System.out.println(oneStat);

        closeLatch.await();
        List<String> paths = zk.getChildren(fatherPath, true);
        System.out.printf("child paths: %s\n", paths);

        zk.delete(fatherPath, -1);
        System.out.println("closing ZooKeeper...");
        zk.close();
    }

    @Test
    public void testWatchers() throws Exception {
        String onePath = "/one";
        String twoPath = "/two";
        setCloseLatch(2);

        // Set the default watcher
        Stat oneStat = zk.exists(onePath, true);
        System.out.printf("%s stat: %s\n", onePath, oneStat);

        // Set a specific watcher
        Stat twoStat = zk.exists(twoPath, new ExistsWatcher(this));
        System.out.printf("%s stat: %s\n", twoPath, twoStat);

        // After creating znode /one in zkCli.sh, the default watcher should print a
        // message.
        // After creating znode /two in zkCli.sh, the default watcher should print a
        // message.
        closeLatch.await();
        zk.delete(onePath, -1);
        zk.delete(twoPath, -1);
        System.out.println("closing ZooKeeper...");
        zk.close();
    }

    /**
     * Two default watchers are set, but at most one get triggered.
     */
    @Test
    public void testGlobalWatcherAtMostTriggerOnce() throws Exception {
        String path = "/three";
        setCloseLatch(1);

        Stat oneStat;
        oneStat = zk.exists(path, true);
        System.out.printf("%s stat for the first exists: %s\n", path, oneStat);
        oneStat = zk.exists(path, true);
        System.out.printf("%s stat for the second exists: %s\n", path, oneStat);
        // After seeing the above output, issue create /three in zkCli. Then one message
        // should be
        // printed by the default watcher.

        closeLatch.await();
        zk.delete(path, -1);
        System.out.println("closing ZooKeeper...");
        zk.close();

    }

    /**
     * Two explicit watchers are set, but at most one get triggered.
     */
    @Test
    public void testExplicitWatcherAtMostTriggerOnce() throws Exception {
        String path = "/four";
        setCloseLatch(1);

        Stat twoStat;
        twoStat = zk.exists(path, new ExistsWatcher(this));
        System.out.printf("%s stat for the first exists: %s\n", path, twoStat);
        twoStat = zk.exists(path, new ExistsWatcher(this));
        System.out.printf("%s stat for the second exists: %s\n", path, twoStat);
        // After seeing the above output, issue create /two in zkCli.

        closeLatch.await();
        zk.delete(path, -1);
        System.out.println("closing ZooKeeper...");
        zk.close();
    }

}

/**
 * Default watcher.
 */
class DefaultWatcher implements Watcher {
    private CountDownLatch startLatch = new CountDownLatch(1);
    private WatcherTests tests;

    public DefaultWatcher(WatcherTests tests) {
        this.tests = tests;
    }

    public void process(WatchedEvent event) {
        System.out.printf("event in default watcher: %s\n", event);
        if (event.getType() == Event.EventType.None && event.getState() == Event.KeeperState.SyncConnected) {
            startLatch.countDown();
            return;
        } else if (event.getType() == Event.EventType.NodeCreated) {
            tests.countDownCloseLatch();
            return;
        }
    }

    public void await() throws InterruptedException {
        startLatch.await();
    }
}

/** Watcher for exists method. */
class ExistsWatcher implements Watcher {
    private WatcherTests tests;

    public ExistsWatcher(WatcherTests tests) {
        this.tests = tests;
    }

    public void process(WatchedEvent event) {
        System.out.printf("event in exists watch: %s\n", event);
        Event.EventType eventType = event.getType();
        if (eventType == Event.EventType.NodeCreated || eventType == Event.EventType.NodeChildrenChanged) {
            tests.countDownCloseLatch();
            return;
        }
    }
}
