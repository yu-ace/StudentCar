package com.example.demo.model;

import com.alibaba.fastjson2.JSON;
import com.example.demo.Demo2Application;
import com.example.demo.service.IStudentService;
import com.example.demo.service.impl.StudentService;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;

public class Session implements Runnable , Closeable {

    private User user;
    private Socket socket;
    private Thread thread;
    private boolean isRunning = true;

    private IStudentService studentService = StudentService.getInstance();

    private Session(){
    }

    public Session(Socket socket){
        this.socket = socket;
        thread = new Thread(this);
        thread.start();
        Message message = new Message(-1,"MESSAGE","欢迎登录驾校系统");
        try {
            socket.getOutputStream().write(JSON.toJSONString(message).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                System.out.println("接收到消息：" + message.trim());
                Message message1 = JSON.parseObject(message.trim(), Message.class);
                switch(message1.getCommand()){
                    case "LOGIN":
                        User user1 = new User();
                        user1.setId(Demo2Application.getId());
                        user1.setNickName(message1.getNickName());
                        this.setUser(user1);

                        Message message2 = new Message();
                        message2.setUserId(user1.getId());
                        message2.setCommand("LOGIN_OK");
                        socket.getOutputStream().write(JSON.toJSONString(message2).getBytes());
                        break;
                    case "NEW_STUDENT":
                        studentService.newStudent(message1.getId(), message1.getName());
                        sendSystemMessage("创建成功");
                        break;
                    case "ADD_GRADE":
                        studentService.addGrade(message1.getId(), message1.getN(), message1.getGrade());
                        sendSystemMessage("添加成功");
                        break;
                    case "LIST":
                        List<Student> studentList = studentService.getStudentList();
                        String ls = "";
                        for(Student student : studentList){
                            String list = String.format("%d %s %d %d %d %d",student.getId(),student.getName(),
                                    student.getClass1Grade(),student.getClass2Grade(),student.getClass3Grade(),
                                    student.getClass4Grade());
                            ls = ls + list + "\n";
                        }
                        sendSystemMessage(ls);
                        break;
                    case "AVE":
                        double[] ave = studentService.ave();
                        String ls1 = "";
                        for(int i = 1;i < 5;i++){
                            String a = String.format("科目%d的平均分为%.2f",i,ave[i - 1]);
                            ls1 = ls1 + a + "\n";
                        }
                        sendSystemMessage(ls1);
                        break;
                    case "MAX":
                        Student[] student = studentService.max();
                        String ls2 = "";
                        String a = String.format("科目%d的最高分为%d，由%s创造",
                                1,student[0].getClass1Grade(),student[0].getName());
                        String b = String.format("科目%d的最高分为%d，由%s创造",
                                2,student[1].getClass2Grade(),student[1].getName());
                        String c = String.format("科目%d的最高分为%d，由%s创造",
                                3,student[2].getClass3Grade(),student[2].getName());
                        String d = String.format("科目%d的最高分为%d，由%s创造",
                                4,student[3].getClass4Grade(),student[3].getName());
                        ls2 = a +"\n" + b +"\n" + c +"\n" + d;
                        sendSystemMessage(ls2);
                        break;
                    case "MIN":
                        Student[] student1 = studentService.min();
                        String ls3 = "";
                        String a1 = String.format("科目%d的最低分为%d，由%s创造",
                                1,student1[0].getClass1Grade(),student1[0].getName());
                        String b1 = String.format("科目%d的最低分为%d，由%s创造",
                                2,student1[1].getClass2Grade(),student1[1].getName());
                        String c1 = String.format("科目%d的最低分为%d，由%s创造",
                                3,student1[2].getClass3Grade(),student1[2].getName());
                        String d1 = String.format("科目%d的最低分为%d，由%s创造",
                                4,student1[3].getClass4Grade(),student1[3].getName());
                        ls3 = a1 +"\n" + b1 +"\n" + c1 +"\n" + d1;
                        sendSystemMessage(ls3);
                        break;
                    case "PASSED":
                        List<Student> passed = studentService.passed();
                        String ls4 = " ";
                        for(Student student2:passed){
                            String a2 = String.format("%s\n",student2.getName());
                            ls4 = ls4 + a2;
                        }
                        sendSystemMessage(ls4);
                        break;
                    case "LOGOUT":
                        isRunning = false;
                        System.out.printf("用户%s退出\n",message1.getNickName());
                        sendSystemMessage("BYE");
                        this.close();
                        break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendSystemMessage(String message){
        Message message1 = new Message(-1,"MESSAGE",message);
        try {
            socket.getOutputStream().write(JSON.toJSONString(message1).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
