package com.lvr.livecircle.bean;


/**
 * eventbus 传递的父类
 */

public class ObjectEvent<T> {
    private  T object;
    private  int type;
    private  boolean result;

    public ObjectEvent(int type, T object, boolean result) {
        this.object = object;
        this.type = type;
        this.result=result;
    }
    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

}