package com.ldc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
*   BIO通信的服务端
* */
public class Server {

    private static ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
    );

    public static void main(String[] args) throws IOException {
        //服务器必备
        ServerSocket serverSocket = new ServerSocket();
        //绑定监听端口
        serverSocket.bind(new InetSocketAddress(8888));
        System.out.println("Listening on port 8888");
        while (true) {
            executor.execute(new ServerTask(serverSocket.accept()));
        }
    }
    private static class ServerTask implements Runnable {

        private Socket socket = null;

        public ServerTask(Socket socket) {this.socket = socket;}

        @Override
        public void run() {
            //拿客户端通讯的输入输出流
            try ( ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                  ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());){
                //服务端的输入
                String msg = in.readUTF();
                System.out.println("Server received: " + msg);
                out.writeUTF(msg);
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
