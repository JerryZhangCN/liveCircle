package com.lvr.livecircle.home.present;

import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.User;

/**
 * 与服务器交互的接口
 */

public interface RegisterPresent {

    void register(User user);
    void login(User user);
    void getResource(Resources resources);

}
