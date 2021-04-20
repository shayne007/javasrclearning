package com.feng.javasrc.io.udp.chat;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description
 */

public class TalkTeacher {

    public static void main(String[] args) {
        new Thread(new TalkSend(5555, 8888, "localhost")).start();
        new Thread(new TalkReceive(9999, "xiaoming")).start();
    }
}