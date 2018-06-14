package com.lvr.livecircle.api;


import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Notice;
import com.lvr.livecircle.bean.NoticeComment;
import com.lvr.livecircle.bean.Order;
import com.lvr.livecircle.bean.ResourceType;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.bean.STS;
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
    Call<BaseResponse<User>> login(@Body User user);

    /**
     * 获取首页数据
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getMsg)
    Call<BaseResponse> getData(@Body Resources resources);

    /**
     * 获取用户发布的资源列表
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getMyResource)
    Call<BaseResponse<List<ResponseResource>>> getMyResource(@Body Resources resources);


    /**
     * 获取用户的买入列表
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getMyOrderList)
    Call<BaseResponse<List<Order>>> getMyOrderList(@Body Resources resources);

    /**
     * 获取用户卖出的订单列表
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getMyShellList)
    Call<BaseResponse<List<Order>>> getMyShellList(@Body Resources resources);

    /**
     * 通过id查询资源详情
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
        @POST(ApiString.getResourceById)
    Call<BaseResponse<List<ResponseResource>>> getResourceById(@Body Resources resources);

    /**
     * 下订单
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.createOrder)
    Call<BaseResponse> createOrder(@Body Resources resources);

    /**
     * 获取通告列表
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getNoticeList)
    Call<BaseResponse<List<Notice>>> getNoticeList(@Body Resources resources);

    /**
     * 获取通告评论列表
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getNoticeComment)
    Call<BaseResponse<List<NoticeComment>>> getNoticeComment(@Body Resources resources);

    /**
     * 对通告进行评论
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.pushComment)
    Call<BaseResponse> pushComment(@Body Resources resources);

    /**
     * 获取STS数据
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getSTS)
    Call<STS> getSTS(@Body Resources resources);

    /**
     * 更新用户信息
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.uploadUserMsg)
    Call<BaseResponse> updateUserMsg(@Body User user);

    /**
     * 获取资源类别
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getResourceType)
    Call<BaseResponse<List<ResourceType>>> getResourceType(@Body Resources resources);

    /**
     * 创建资源
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.createResource)
    Call<BaseResponse> createResource(@Body Resources resources);
    /**
     * 获取收藏资源列表
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.getCollectionList)
    Call<BaseResponse<List<ResponseResource>>> getCollectionResource(@Body Resources resources);
    /**
     * 创建资源
     * @param
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST(ApiString.collectionResource)
    Call<BaseResponse> collectionResource(@Body Resources resources);
}

