package com.feng.jdk.io;

import java.nio.ByteBuffer;

/**
 * @author fengsy
 * @date 1/20/21
 * @Description
 */

public class ByteBufferDemo {

    public static void main(String[] args) {

        // 分配10字节大小内存空间
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 输出初始化后position的值
        System.out.println("初始化position : " + buffer.position());
        // 输出初始化收limit的值
        System.out.println("初始化limit : " + buffer.limit());
        // 输出初始化后capacity的值
        System.out.println("初始化capacity : " + buffer.capacity());
        // 输出初始化后ByteBuffer内容
        printBuffer(buffer);

        // 调用rewind()之前指针指向下标9即位置10,已经是最大容量
        // 调用rewind()之后将指针移动到下标0即位置1
        // buffer.rewind();
        System.out
                .println("position:" + buffer.position() + ",limit:" + buffer.limit() + ",capacity:" + buffer.capacity());
        // 执行写入操作,指针会自动移动
        buffer.putChar('a');
        // buffer.put((byte)'a');
        // 输出指针position,指针指向下标2即位置3
        System.out.println("写入字符'a'后,position位置为:" + buffer.position());
        printBuffer(buffer);
        buffer.putChar('啊');
        // 输出指针position,指针指向下标4即位置5
        System.out.println("写入字符'啊'后,position位置为:" + buffer.position());
        printBuffer(buffer);
        // 将当前位置设置为EOF,指针移动到下标0即位置1
        buffer.flip();
        System.out
                .println("position:" + buffer.position() + ",limit:" + buffer.limit() + ",capacity:" + buffer.capacity());
        // 上一行代码相当于下面两句
        // buffer.limit(4);
        // buffer.position(0);

        // 输出ByteBuffer内容,即0 61 55 4a
        printBuffer(buffer);
        // 将指针移动到下标1即位置2
        buffer.position(1);
        System.out
                .println("position:" + buffer.position() + ",limit:" + buffer.limit() + ",capacity:" + buffer.capacity());

        // 进行compact压缩操作,compact操作会将EOF位置重置为最大容量10
        // 注意:该压缩操作是将下标1即位置2到位置4的值移动到位置1到位置3,位置4上的值4a不变
        buffer.compact();
        // 输出ByteBuffer内容,即61 55 4a 4a 0 0 0 0 0 0(注意:未覆盖到的位置4的值4a不变)
        printBuffer(buffer);

        // 注意:执行压缩compact操作后指针指向下标3即位置4,继续写入数据时会覆盖数据
        System.out
                .println("position:" + buffer.position() + ",limit:" + buffer.limit() + ",capacity:" + buffer.capacity());

    }

    /**
     * 输出ByteBuffer内容
     *
     * @param buffer
     */
    public static void printBuffer(ByteBuffer buffer) {

        // 记录当前位置
        int position = buffer.position();
        // 指针移动到0
        buffer.position(0);
        // 循环输出每个字节内容
        for (int i = 0; i < buffer.limit(); i++) {
            // 读取操作,指针会自动移动
            byte b = buffer.get();
            System.out.print(Integer.toHexString(b) + " ");
        }
        // 指针再移动到标记位置
        buffer.position(position);
        System.out.println();
    }
}