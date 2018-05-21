package com.lvr.livecircle.api;


import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.User;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * retrofit 接口连接类，负责与服务器进行交互
 */
public interface SyncService {
    /**
     * 注册
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.register)
    Call<BaseResponse> register(@Body User user);

    /**
     * 登录
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.login)
    Call<BaseResponse> login(@Body User user);

    /**
     * 获取首页数据
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getMsg)
    Call<BaseResponse> getData(@Body Resources resources);


}

