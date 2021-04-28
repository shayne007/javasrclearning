package com.feng.javasrc.zookeeper.lock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ZkDistributeLock
 * @Description: Zookeeper分布式锁
 * @Author: feng
 */
@Slf4j
public class ZkDistributeLock implements Lock, Watcher {

    /**
     * zk服务集群
     */
    private static String zkServers = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

    /**
     * 分布式锁的根节点
     */
    private static final String LOCK_ROOT_PATH = "/tLock";

    /**
     * 分布式锁的前缀
     */
    private static final String LOCK_PREFIX = "-lk-";

    /**
     * 连接zookeeper服务器的超时时间
     */
    private static final int DEFAULT_SESSION_TIMEOUT = 30000;

    public static final String DEFAULT_LOCK_NAME = "task";

    public static final String masterIp = "127.0.0.1";

    /**
     * zk客户端
     */
    private ZooKeeper zk;

    /**
     * 竞争资源的标志
     */
    private String lockName;

    /**
     * 等待前一个锁
     */
    private String waitNode;

    /**
     * 当前锁
     */
    private String myZnode;

    /**
     * 计数器
     */
    private CountDownLatch latch;

    /**
     * 连接zookeeper服务器的超时时间
     */
    private int sessionTimeout;

    public void init() {
        try {
            log.info(">>>>> zkservers:{}", zkServers);
            zk = new ZooKeeper(zkServers, sessionTimeout, this);
            Stat stat = zk.exists(LOCK_ROOT_PATH, false);
            if (stat == null) {
                // 创建根节点
                zk.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>Exception:{}", e.getMessage());
        }
    }

    public void destroy() {
        this.unlock();
    }

    public ZkDistributeLock() {
        this(DEFAULT_LOCK_NAME);
    }

    public ZkDistributeLock(String lockName) {
        this(lockName, DEFAULT_SESSION_TIMEOUT);
    }

    /**
     * @param lockName
     * @param sessionTimeout
     */
    public ZkDistributeLock(String lockName, int sessionTimeout) {
        if (lockName.contains(LOCK_PREFIX)) {
            log.error(">>>>>>>>>Exception:{}", "lockName 不能包含[" + LOCK_PREFIX + "]");
        }
        this.lockName = lockName;
        this.sessionTimeout = sessionTimeout;
    }

    @Override
    public void lock() {
        if (this.tryLock()) {

            return;
        } else {
            try {
                waitForLock(waitNode, sessionTimeout);
            } catch (Exception e) {
                log.error(">>>>>>>>>Exception:{}", e.getMessage());
            }
        }

    }

    @Override
    public void lockInterruptibly() {
        this.lock();
    }

    @Override
    public boolean tryLock() {
        boolean ret = false;
        try {
            // 创建临时子节点---zookeeper临时有序节点
            myZnode = zk.create(LOCK_ROOT_PATH + "/" + lockName + LOCK_PREFIX, masterIp.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            // 取出所有子节点
            List<String> subNodes = zk.getChildren(LOCK_ROOT_PATH, false);
            if (subNodes.size() == 1) {
                ret = true;
            }
            // 取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<String>();
            for (String node : subNodes) {
                if (node.split(LOCK_PREFIX)[0].equals(lockName)) {
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);
            // 如果是最小的节点,则表示取得锁
            if (myZnode.equals(LOCK_ROOT_PATH + "/" + lockObjNodes.get(0))) {
                ret = true;
                return ret;
            }
            log.info("lockObjNodes :{}", lockObjNodes);
            if (lockObjNodes.size() > 1) {
                // 如果不是最小的节点，找到比自己小1的节点
                String subMyZnode = myZnode.substring(myZnode.lastIndexOf("/") + 1);
                log.info("current node :{}", subMyZnode);
                waitNode = lockObjNodes.get(Collections.binarySearch(lockObjNodes, subMyZnode) - 1);
            }
        } catch (KeeperException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
        } catch (InterruptedException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
        }
        if (ret) {
            log.info("================>>> get {} lock....", lockName);
        } else {
            log.info("================>>> cann't get {} lock....", lockName);
        }
        return ret;
    }

    public boolean hasCurrentLock() {
        boolean ret = false;
        // 取出所有子节点
        try {
            List<String> subNodes = zk.getChildren(LOCK_ROOT_PATH, false);

            if (subNodes == null || subNodes.size() <= 1) {
                log.info("================>>> get {} lock....", lockName);
                return true;
            }
            // 取出所有lockName的锁
            List<String> lockObjNodes = new ArrayList<>();
            for (String node : subNodes) {
                if (node.split(LOCK_PREFIX)[0].equals(lockName)) {
                    lockObjNodes.add(node);
                }
            }
            Collections.sort(lockObjNodes);
            // 如果是最小的节点,则表示取得锁
            if (myZnode.equals(LOCK_ROOT_PATH + "/" + lockObjNodes.get(0))) {
                ret = true;
            }
        } catch (Exception e) {
            log.error(">>>>>> Exception:{}", e.getMessage());
        }
        if (ret) {
            log.info("================>>> get {} lock....", lockName);
        } else {
            log.info("================>>> cann't get {} lock....", lockName);
        }
        return ret;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        try {
            if (tryLock()) {
                return true;
            } else {
                return waitForLock(waitNode, time);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void unlock() {
        try {
            zk.delete(myZnode, -1);
            log.info(">>>>>>>>>>> delete znode:{}", myZnode);
            myZnode = null;
            zk.close();
        } catch (InterruptedException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
            e.printStackTrace();
        } catch (KeeperException e) {
            log.error(">>>>>>>>> Exception:{}", e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * @param waitNode
     *            等待节点
     * @param waitTime
     *            等待时间
     * @return
     * @throws Exception
     */
    private boolean waitForLock(String waitNode, long waitTime) throws Exception {
        Stat stat = zk.exists(LOCK_ROOT_PATH + "/" + waitNode, true);
        // 判断比自己小一个数的节点是否存在,如果不存在则无需等待锁
        if (stat != null) {
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch = null;
        }
        return true;
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * zookeeper的监视器
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        if (this.latch != null) {
            // 监视到有节点删除，计数器减1
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                this.latch.countDown();
            }
        }

    }

    public static void main(String[] args) {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue(10));
        for (int i = 0; i < 4; i++) {
            int threadNumber = i;
            executor.execute(() -> {
                ZkDistributeLock zkDistributeLock = new ZkDistributeLock("test");
                zkDistributeLock.init();
                // 首次获取锁
                zkDistributeLock.lock();
                log.info("thread {} get lock:{}", threadNumber, zkDistributeLock.myZnode);
                if (zkDistributeLock.hasCurrentLock()) {
                    log.info("this node has got the lock :{}", zkDistributeLock.myZnode);
                    try {
                        Thread.sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    zkDistributeLock.unlock();
                } else {
                    zkDistributeLock.lock();
                    log.info("thread {} get lock:{}", threadNumber, zkDistributeLock.myZnode);
                    if (zkDistributeLock.hasCurrentLock()) {
                        log.info("this node has got the lock :{}", zkDistributeLock.myZnode);
                        try {
                            Thread.sleep(5000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        zkDistributeLock.unlock();
                    }
                }
            });
        }

    }
}
