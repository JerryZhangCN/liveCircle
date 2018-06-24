package com.lvr.livecircle.home.present;

import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.bean.User;

/**
 * 与服务器交互的接口
 */

public interface RegisterPresent {

    void register(User user);
    void login(User user);
    void getResource(Resources resources);
    void getMyResource(Resources resources);
    void getMyOrderList(Resources resources);
    void getResourceById(Resources resources);
    void createOrder(Resources resources);
    void getMyShellList(Resources resources);
    void getNoticeList(Resources resources);
    void getNoticeComment(Resources resources);
    void pushNoticeComment(Resources resources);
    void getSTS(Resources resources);
    void updateUserMsg(User user);
    void getResourceType(Resources resources);
    void createResource(Resources resources);
    void getUserCollection(Resources resources);
    void collectionResource(Resources resources);
    void sureShip(Resources resources);
}
