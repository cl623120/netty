package com.ldc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        //客户端启动必备
        Socket socket =  null;
        //实例化与服务器通信的输入输出流
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        //服务器的地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        try {
            socket = new Socket();
            socket.connect(address);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            //向服务器输出请求
            out.writeUTF("COYG!!!!!!!!");
            out.flush();

            //接收服务器的输出
            System.out.println(in.readUTF());
        } finally {
            if (socket != null) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
}
