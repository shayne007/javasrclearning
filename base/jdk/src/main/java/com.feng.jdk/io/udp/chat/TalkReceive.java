package com.feng.jdk.io.udp.chat;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description
 */
public class TalkReceive implements Runnable {
    DatagramSocket datagramSocket = null;
    private int port;
    private String msgFrom;

    public TalkReceive(int port, String msgFrom) {
        this.port = port;
        this.msgFrom = msgFrom;
        // 1、建立连接
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 2、接收数据包
                byte[] buffer = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
                datagramSocket.receive(datagramPacket);
                // 3、断开连接
                byte[] data = datagramPacket.getData();
                String receiveData = new String(data, 0, data.length);
                System.out.println(msgFrom + ": " + receiveData);
                if (receiveData.equals("bye")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        datagramSocket.close();
    }
}