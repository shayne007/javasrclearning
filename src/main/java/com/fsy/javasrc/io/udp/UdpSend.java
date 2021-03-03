package com.fsy.javasrc.io.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author fengsy
 * @date 1/26/21
 * @Description
 */

public class UdpSend {
    public static void main(String[] args) throws Exception {
        // 1、建立连接
        DatagramSocket datagramSocket = new DatagramSocket(9888);
        // 2、创建数据包，从键盘输入
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        int port = 9887;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String s = bufferedReader.readLine();
            DatagramPacket datagramPacket = new DatagramPacket(s.getBytes(), 0, s.getBytes().length, inetAddress, 9887);
            // 3、发送数据
            datagramSocket.send(datagramPacket);
            if (s.equals("bye")) {
                break;
            }
        }
        // 4、关闭数据
        datagramSocket.close();
    }
}