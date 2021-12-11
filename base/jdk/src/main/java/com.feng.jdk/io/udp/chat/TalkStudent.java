package com.feng.jdk.io.udp.chat;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description
 */

public class TalkStudent {
    public static void main(String[] args) {
        new Thread(new TalkSend(7777, 9999, "localhost")).start();
        new Thread(new TalkReceive(8888, "Mrs Yang")).start();
    }
}