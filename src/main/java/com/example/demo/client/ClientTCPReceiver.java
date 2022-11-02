package com.example.demo.client;

import com.alibaba.fastjson2.JSON;
import com.example.demo.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientTCPReceiver implements Runnable{
    private Socket socket;

    public ClientTCPReceiver(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true){
            byte[] buffer = new byte[1024];
            try {
                InputStream in = socket.getInputStream();
                in.read(buffer);
                String message = new String(buffer);
                Message message1 = JSON.parseObject(message.trim(),Message.class);
                if(message1.getCommand().equals("BYE")){
                    System.out.printf("服务器已断开，%s再见\n",message1.getNickName());
                    System.exit(0);
                }else if(message1.getCommand().equals("LOGIN_OK")){
                    int id = message1.getUserId();
                    ClientHandler.userId = id;
                }else if(message1.getCommand().equals("MESSAGE")){
                    System.out.println(message1.getConnect());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
