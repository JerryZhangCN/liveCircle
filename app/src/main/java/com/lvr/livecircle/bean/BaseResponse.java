package com.lvr.livecircle.bean;

import java.util.List;

/**
 * Created by jerry on 2018/4/18.
 * 服务器返回数据的通用父类模板，包含通用的固定格式
 */

public class BaseResponse<T> {

    //服务器返回码
    private int code;

    //服务器返回消息
    private String msg;

    //泛型包装的消息体，针对服务器返回的各种类型的数据进行自动匹配，使用时以泛型强转即可
    private T info;

    public class info<T>{
        private List<T> list;

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }
}
