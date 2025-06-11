package com.feng.jdk.concurrency.patterns.hsynchasync;

import com.feng.jdk.concurrency.patterns.twophaseterminate.reusable.AlarmType;

/**
 * @author fengsy
 * @date 5/19/21
 * @Description
 */
public class CaseRunner {
    public static void main(String[] args) throws InterruptedException {
        AlarmMgr alarmMgr = AlarmMgr.getInstance();
        alarmMgr.init();

        String alarmId = "0000000010";

        alarmMgr.sendAlarm(AlarmType.FAULT, alarmId, "key1=value1;key2=value2");

        Thread.sleep(80);

        alarmMgr.sendAlarm(AlarmType.RESUME, alarmId, "key1=value1;key2=value2");
        Thread.sleep(600);

        alarmMgr.shutdown();
    }
}
