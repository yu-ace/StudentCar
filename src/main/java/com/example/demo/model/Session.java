package com.example.demo.model;

import com.alibaba.fastjson2.JSON;
import com.example.demo.Demo2Application;
import org.springframework.boot.web.context.MissingWebServerFactoryBeanException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.List;

public class Session implements Runnable , Closeable {
    private String nickName;
    private Socket socket;
    private Thread thread;
    private boolean isRunning = true;

    private Session(){
    }

    public Session(Socket socket){
        this.socket = socket;
        thread = new Thread(this);
        thread.start();
        Message message = new Message("SYSTEM","xxx","欢迎使用QQ");
        try {
            socket.getOutputStream().write(JSON.toJSONString(message).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void close() throws IOException {
        System.out.println("用户退出，关闭线程，释放资源");
        Demo2Application.sessionManage.removeSession(this);
        thread.interrupt();
        socket.close();
    }

    @Override
    public void run() {
        while(isRunning){
            try {
                byte[] buffer = new byte[1024];
                InputStream in = socket.getInputStream();
                in.read(buffer);
                String message = new String(buffer);
                Message message1 = JSON.parseObject(message.trim(), Message.class);
                if("SYSTEM".equals(message1.getReceiverName())){
                    if(message1.getMessage().equals("LIST")){
                        List<String> online = Demo2Application.sessionManage.getOnline();
                        String join = String.join("\n",online);
                        Message message2 = new Message("SYSTEM", message1.getNickName(),join);
                        socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
                    }else if(message1.getMessage().equals("LOGIN")){
                        this.setNickName(message1.getNickName());
                    }else if(message1.getMessage().equals("LOGOUT")){
                        System.out.printf("用户%s退出",message1.getNickName());
                        Message message2 = new Message("SYSTEM",message1.getNickName(),"BYE");
                        socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
                        this.close();
                    }
                }else{
                    String receiverName = message1.getReceiverName();
                    Session sessionByNickName = Demo2Application.sessionManage.getSessionByNickName(receiverName);
                    if(sessionByNickName == null){
                        System.out.printf("来自%s的发送给%s的消息：%s，%s",message1.getNickName(),
                                receiverName,message1.getMessage(),"对方不在线，发送失败");
                        Message message2 = new Message("SYSTEM",receiverName,"用户不在线，发送失败！");
                        socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
                    }else{
                        Socket receiverSocket = sessionByNickName.getSocket();
                        receiverSocket.getOutputStream().write(JSON.toJSONString(message1).getBytes());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
