package com.feng.jdk.io.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static int DEFAULT_SERVER_PORT = 12345;
    private static String DEFAULT_SERVER_IP = "127.0.0.1";
    private Socket socket = null;

    /**
     * 连接服务
     */
    public void connect() {
        try {
            // InetAddress inetAddress = InetAddress.getByName(DEFAULT_SERVER_IP);
            // socket = new Socket(inetAddress, DEFAULT_SERVER_PORT);
            socket = new Socket(DEFAULT_SERVER_IP, DEFAULT_SERVER_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public void send(String msg) {

        System.out.println("发送的消息为：" + msg);
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("返回消息为：" + in.readLine());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 关闭连接
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 运行客户端
        new Thread(() -> {
            Client client = new Client();
            while (true) {

                client.connect();// 连接服务端
                String scanner = new Scanner(System.in).nextLine();

                if (!scanner.equals("q")) {
                    client.send(scanner);// 发送消息
                } else {

                    client.close();
                    break;
                }

                client.close();

            }
        }).start();

    }
}
