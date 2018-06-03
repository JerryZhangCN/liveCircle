package com.lvr.livecircle.api;

import com.lvr.livecircle.widget.LoadMoreFooterView;

public class ApiString {


    /*  服务器地址  */
    public static final String SERVICE_URL="http://60.205.185.89:9000/";


    //登录接口
    public static final String login="m/indexApi/login";
    //注册接口
    public static final String register="m/indexApi/register";
    //获取首页数据
    public static final String getMsg="/m/resourcesApi/listResourcesByParm";
    //获取用户发布的资源
    public static final String getMyResource="/m/resourcesApi/listResourcesByUser";
    //获取用户的订单列表
    public static final String getMyOrderList="/m/orderApi/listMyOrderOnMerchant";
    //根据ID获取资源信息
    public static final String getResourceById="/m/resourcesApi/getResourcesById";
    //下订单
    public static final String createOrder="/m/orderApi/addOrder";
    //获取用户的订单列表
    public static final String getMyShellList="/m/orderApi/listMyOrderOnMerchant";

}