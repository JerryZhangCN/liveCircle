package com.lvr.livecircle.bean;

import java.io.Serializable;

/**
 * Created by jerry on 2018/5/21.
 */

public class ResponseResource implements Serializable{
    private String name;
    private String add_time;
    private String credit_number;
    private String price;
    private String img1;
    private String img2;
    private String img3;
    private String id;
    private String user_name;
    //描述
    private String cmt;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getCredit_number() {
        return credit_number;
    }

    public void setCredit_number(String credit_number) {
        this.credit_number = credit_number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }
}
