package com.example.demo.server;

import com.example.demo.model.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionManage {

    private  static SessionManage sessionManage = new SessionManage();
    private SessionManage(){
    }
    public static SessionManage getInstance(){
        return sessionManage;
    }

    private List<Session> sessionList = new ArrayList<>();
    public void addSession(Session session){
        sessionList.add(session);
    }

    public void removeSession(Session session){
        sessionList.remove(session);
    }



}
