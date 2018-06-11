package com.lvr.livecircle.bean;

import java.util.List;

public class Cache {

    private User user;

    private static Cache instance;

    private STS sts;

    private List<ResourceType> resourceTypes;

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

    public STS getSts() {
        return sts;
    }

    public void setSts(STS sts) {
        this.sts = sts;
    }

    public static void setInstance(Cache instance) {
        Cache.instance = instance;
    }

    public List<ResourceType> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(List<ResourceType> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }
}
