package com.example.demo.client;

import com.alibaba.fastjson2.JSON;
import com.example.demo.model.Message;
import com.example.demo.model.User;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler {
    public static int userId = -1;
    private Scanner scanner = new Scanner(System.in);
    ClientTCPReceiver clientTCPReceiver;
    Socket socket;
    Thread thread;

    public void mainLoop() throws IOException {
        clientHandle();
    }

    private void clientHandle() throws IOException {
        System.out.println("请输入服务器IP地址：");
        String ip = scanner.next();
        System.out.println("请输入监听的端口号：");
        int port = scanner.nextInt();
        System.out.println("请输入你的昵称：");
        String userName = scanner.next();

        socket = new Socket(ip, port);
        clientTCPReceiver = new ClientTCPReceiver(socket);
        thread = new Thread(clientTCPReceiver);
        thread.start();
        Message message = new Message();
        message.setNickName(userName);
        message.setCommand("LOGIN");
        byte[] messageByte = JSON.toJSONString(message).getBytes();
        socket.getOutputStream().write(messageByte);
        while (userId == -1) {
            System.out.println("等待服务器返回注册信息，请稍后。。。");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (true) {
            printHelp();
            String str = scanner.next();
            switch (str) {
                case "1":
                    System.out.println("请输入学生的id：");
                    int id = scanner.nextInt();
                    System.out.println("请输入学生的姓名：");
                    String name = scanner.next();
                    Message message1 = new Message();
                    message1.setCommand("NEW_STUDENT");
                    message1.setId(id);
                    message1.setName(name);
                    byte[] messageByte1 = JSON.toJSONString(message1).getBytes();
                    socket.getOutputStream().write(messageByte1);
                    break;
                case "2":
                    Message message2 = new Message();
                    System.out.println("请输入学生id：");
                    int id1 = scanner.nextInt();
                    System.out.println("请输入考试科目：");
                    int n = scanner.nextInt();
                    System.out.println("请输入考试成绩：");
                    int grade = scanner.nextInt();
                    message2.setCommand("ADD_GRADE");
                    message2.setId(id1);
                    message2.setN(n);
                    message2.setGrade(grade);
                    byte[] messageByte2 = JSON.toJSONString(message2).getBytes();
                    socket.getOutputStream().write(messageByte2);
                    break;
                case "3":
                    Message message3 = new Message();
                    message3.setCommand("LIST");
                    byte[] messageByte3 = JSON.toJSONString(message3).getBytes();
                    socket.getOutputStream().write(messageByte3);
                    break;
                case "4":
                    Message message4 = new Message();
                    message4.setCommand("AVE");
                    byte[] messageByte4 = JSON.toJSONString(message4).getBytes();
                    socket.getOutputStream().write(messageByte4);
                    break;
                case "5":
                    Message message5 = new Message();
                    message5.setCommand("MAX");
                    byte[] messageByte5 = JSON.toJSONString(message5).getBytes();
                    socket.getOutputStream().write(messageByte5);
                    break;
                case "6":
                    Message message6 = new Message();
                    message6.setCommand("MIN");
                    byte[] messageByte6 = JSON.toJSONString(message6).getBytes();
                    socket.getOutputStream().write(messageByte6);
                    break;
                case "7":
                    Message message7 = new Message();
                    message7.setCommand("PASSED");
                    byte[] messageByte7 = JSON.toJSONString(message7).getBytes();
                    socket.getOutputStream().write(messageByte7);
                    break;
                case "q":
                    Message message8 = new Message();
                    message8.setNickName(message.getNickName());
                    message8.setCommand("LOGOUT");
                    byte[] messageByte8 = JSON.toJSONString(message8).getBytes();
                    socket.getOutputStream().write(messageByte8);
                    System.exit(0);
                    break;
            }
        }
    }

    private void printHelp() {
        System.out.println("输入1 录入学生信息");
        System.out.println("输入2 录入学生成绩");
        System.out.println("输入3 查看学生成绩列表");
        System.out.println("输入4 查看学生平均值");
        System.out.println("输入5 查看各科最大值");
        System.out.println("输入6 查看最小值");
        System.out.println("输入7 查看通过人数");
        System.out.println("输入q 退出");
    }




}
