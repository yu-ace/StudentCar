package com.example.demo.server;

import com.example.demo.model.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionManage {
    private List<Session> sessionList = new ArrayList<>();

    public List<String> getOnline(){
        List<String> online = new ArrayList<>();
        for(Session session: sessionList){
            online.add(session.getNickName());
        }
        return online;
    }

    public void addSession(Session session){
        sessionList.add(session);
    }

    public void removeSession(Session session){
        sessionList.remove(session);
    }

    public Session getSessionByNickName(String nickName){
        if(nickName == null && nickName.length() == 0){
            return null;
        }
        for(Session session : sessionList){
            if(session.getNickName().equals(nickName)){
                return session;
            }
        }
        return null;
    }

}
