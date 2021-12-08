package com.feng.concurrency.patterns.guardedsuspension;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.feng.concurrency.patterns.guardedsuspension.reusable.Blocker;
import com.feng.concurrency.patterns.guardedsuspension.reusable.ConditionVarBlocker;
import com.feng.concurrency.patterns.guardedsuspension.reusable.GuardedAction;
import com.feng.concurrency.patterns.guardedsuspension.reusable.Predicate;
import com.feng.concurrency.util.Debug;
import com.feng.concurrency.util.Tools;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 使用Guarded Suspension模式实现告警代理模块： 主线程发送告警信息给告警服务器； 发送消息前需要与服务器建立连接； 定时检测连接状态
 *
 * @author fengsy
 * @date 5/17/21
 * @Description
 */
public class AlarmAgent {
    private volatile boolean connectedToServer = false;
    private final Predicate agentConnected = new Predicate() {
        @Override
        public boolean evaluate() {
            return connectedToServer;
        }
    };

    private final Blocker blocker = new ConditionVarBlocker();

    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
        new ThreadFactoryBuilder().setNameFormat("example-schedule-pool-%d").setDaemon(true).build());

    /**
     * 每次调用都会创建一个新的GuardedAction，会增加JVM中Eden区垃圾回收的负担
     * 
     * @param alarm
     * @throws Exception
     */
    public void sendAlarm(final AlarmInfo alarm) throws Exception {
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            @Override
            public Void call() {
                doSendAlarm(alarm);
                return null;
            }
        };

        blocker.callWithGuard(guardedAction);
    }

    // 通过网络连接将告警信息发送给告警服务器
    private void doSendAlarm(AlarmInfo alarm) {
        // 省略其他代码
        Debug.info("sending alarm " + alarm);

        // 模拟发送告警至服务器的耗时
        try {
            Thread.sleep(50);
        } catch (Exception e) {

        }
    }

    public void init() {
        // 省略其他代码

        // 告警连接线程
        Thread connectingThread = new Thread(new ConnectingTask());

        connectingThread.start();

        executorService.scheduleAtFixedRate(new HeartbeatTask(), 60, 2, TimeUnit.SECONDS);
    }

    public void disconnect() {
        // 省略其他代码
        Debug.info("disconnected from alarm server.");
        connectedToServer = false;
    }

    protected void onConnected() {
        try {
            blocker.signalAfter(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    connectedToServer = true;
                    Debug.info("connected to server");
                    return Boolean.TRUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onDisconnected() {
        connectedToServer = false;
    }

    /**
     * 负责与告警服务器建立网络连接
     */
    private class ConnectingTask implements Runnable {
        @Override
        public void run() {
            // 省略其他代码

            // 模拟连接操作耗时
            Tools.randomPause(100, 40);

            onConnected();
        }
    }

    /**
     * 心跳定时任务：定时检查与告警服务器的连接是否正常，发现连接异常后自动重新连接
     */
    private class HeartbeatTask implements Runnable {
        // 省略其他代码

        @Override
        public void run() {
            // 省略其他代码

            if (!testConnection()) {
                onDisconnected();
                reconnect();
            }

        }

        private boolean testConnection() {
            // 省略其他代码

            return true;
        }

        private void reconnect() {
            ConnectingTask connectingThread = new ConnectingTask();

            // 直接在心跳定时器线程中执行
            connectingThread.run();
        }

    }
}
