package com.lvr.livecircle.bean;

/**
 * Created by jerry on 2018/5/21.
 */

public class Resources {
    private int page;
    private String order_by_price;
    private String order_by_credit;
    private String order_by_time;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrder_by_price() {
        return order_by_price;
    }

    public void setOrder_by_price(String order_by_price) {
        this.order_by_price = order_by_price;
    }

    public String getOrder_by_credit() {
        return order_by_credit;
    }

    public void setOrder_by_credit(String order_by_credit) {
        this.order_by_credit = order_by_credit;
    }

    public String getOrder_by_time() {
        return order_by_time;
    }

    public void setOrder_by_time(String order_by_time) {
        this.order_by_time = order_by_time;
    }
}