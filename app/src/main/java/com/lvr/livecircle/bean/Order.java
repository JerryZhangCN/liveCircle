package com.lvr.livecircle.bean;

public class Order {
    //订单id
    private String id;
    //订单单号
    private String orderno;
    //交易时间
    private String itime;
    //资源金额
    private String resources_price;
    //资源信用
    private String resources_creditnumber;
    //资源名字
    private String resources_name;
    //资源图片
    private String resources_img1;
    //资源介绍
    private String resources_cmt;
    //买家名字
    private String consumename;
    //订单状态 1 为进行中  2  取消  3完成
    private String order_status;
    //配送状态
    private String shipping_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getItime() {
        return itime;
    }

    public void setItime(String itime) {
        this.itime = itime;
    }

    public String getResources_price() {
        return resources_price;
    }

    public void setResources_price(String resources_price) {
        this.resources_price = resources_price;
    }

    public String getResources_creditnumber() {
        return resources_creditnumber;
    }

    public void setResources_creditnumber(String resources_creditnumber) {
        this.resources_creditnumber = resources_creditnumber;
    }

    public String getResources_name() {
        return resources_name;
    }

    public void setResources_name(String resources_name) {
        this.resources_name = resources_name;
    }

    public String getResources_img1() {
        return resources_img1;
    }

    public void setResources_img1(String resources_img1) {
        this.resources_img1 = resources_img1;
    }

    public String getResources_cmt() {
        return resources_cmt;
    }

    public void setResources_cmt(String resources_cmt) {
        this.resources_cmt = resources_cmt;
    }

    public String getConsumename() {
        return consumename;
    }

    public void setConsumename(String consumename) {
        this.consumename = consumename;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }
}
