package com.fsy.javasrc.io.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description
 */

public class UdpRecieve {
    public static void main(String[] args) throws Exception {
        // 1、建立连接
        DatagramSocket datagramSocket = new DatagramSocket(9887);
        while (true) {
            // 2、接收数据包
            byte[] buffer = new byte[1024];
            DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
            datagramSocket.receive(datagramPacket);
            // 3、断开连接
            byte[] data = datagramPacket.getData();
            String receiveData = new String(data, 0, data.length);
            System.out.println(receiveData);
            if (receiveData.equals("bye")) {
                break;
            }
        }
        datagramSocket.close();
    }
}