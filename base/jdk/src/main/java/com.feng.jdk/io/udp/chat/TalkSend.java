package com.feng.io.udp.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description
 */

public class TalkSend implements Runnable {
    DatagramSocket datagramSocket = null;
    BufferedReader bufferedReader = null;
    private int fromPort;
    private int toPort;
    private String toIp;

    public TalkSend(int fromPort, int toPort, String toIp) {
        this.fromPort = fromPort;
        this.toPort = toPort;
        this.toIp = toIp;
        // 1、建立连接
        try {
            datagramSocket = new DatagramSocket(fromPort);
            // 2、创建数据包，从键盘输入
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String s = bufferedReader.readLine();
                DatagramPacket datagramPacket =
                    new DatagramPacket(s.getBytes(), 0, s.getBytes().length, new InetSocketAddress(toIp, toPort));
                // 3、发送数据
                datagramSocket.send(datagramPacket);
                if (s.equals("bye")) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 4、关闭数据
        datagramSocket.close();
    }
}