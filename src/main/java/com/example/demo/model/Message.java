package com.example.demo.model;

public class Message {
    private int userId;
    private String nickName;
    //命令
    private String command;
    //内容
    private String connect;
    private int id;
    private String name;
    private int n;
    private int grade;

    public Message(){
    }


    public Message(int userId, String command, String connect) {
        this.userId = userId;
        this.command = command;
        this.connect = connect;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserName(int userId) {
        this.userId = userId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getConnect() {
        return connect;
    }

    public void setConnect(String connect) {
        this.connect = connect;
    }
}
