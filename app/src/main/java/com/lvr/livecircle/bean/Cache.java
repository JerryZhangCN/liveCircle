package com.lvr.livecircle.bean;

public class Cache {

    private User user;

    private static Cache instance;

    public static Cache getInstance() {
        if(instance==null)
           instance=new Cache();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
