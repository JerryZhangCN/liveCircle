package com.lvr.livecircle.home.present;

import com.lvr.livecircle.api.ServiceClient;
import com.lvr.livecircle.api.SyncService;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Order;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.bean.User;
import com.lvr.livecircle.utils.StatusCode;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit2.Response;

/**
 * 与服务器交互的实现类
 */

public class RegisterPresentImpl implements RegisterPresent {
    @Override
    public void register(final User user) {
        doConnect(StatusCode.register, user);
    }

    @Override
    public void login(User user) {
        doConnect(StatusCode.login, user);
    }

    @Override
    public void getResource(Resources resources) {
        doConnect(StatusCode.getResources, resources);
    }

    @Override
    public void getMyResource(Resources resources) {
        doConnect(StatusCode.getMyResources, resources);
    }

    @Override
    public void getMyOrderList(Resources resources) {
        doConnect(StatusCode.getMyOrderList, resources);
    }

    @Override
    public void getResourceById(Resources resources) {
        doConnect(StatusCode.getResourceById, resources);
    }

    @Override
    public void createOrder(Resources resources) {
        doConnect(StatusCode.createOrder, resources);
    }

    @Override
    public void getMyShellList(Resources resources) {
        doConnect(StatusCode.getShellList, resources);
    }


    /**
     * 网络请求
     *
     * @param type
     * @param object
     */
    private void doConnect(final int type, final Object object) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SyncService syncService = ServiceClient.getService(SyncService.class);
                    Response<BaseResponse> response = null;
                    switch (type) {
                        case StatusCode.register: {
                            response = syncService.register((User) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.register, response.body(), true));
                            break;
                        }
                        case StatusCode.login: {
                            Response<BaseResponse<User>> userResponse = null;
                            userResponse = syncService.login((User) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.login, userResponse.body(), true));
                            break;
                        }
                        case StatusCode.getResources: {
                            response = syncService.getData((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getResources, response.body(), true));
                            break;
                        }
                        case StatusCode.getMyResources: {
                            Response<BaseResponse<List<Resources>>> resourceResponse = syncService.getMyResource((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getMyResources, resourceResponse.body(), true));
                            break;
                        }

                        case StatusCode.getMyOrderList: {
                            Response<BaseResponse<List<Order>>> resourceResponse = syncService.getMyOrderList((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getMyOrderList, resourceResponse.body(), true));
                            break;
                        }

                        case StatusCode.getShellList: {
                            Response<BaseResponse<List<Order>>> resourceResponse = syncService.getMyShellList((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getShellList, resourceResponse.body(), true));
                            break;
                        }

                        case StatusCode.getResourceById: {
                            Response<BaseResponse<List<ResponseResource>>> resourceResponse = syncService.getResourceById((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getResourceById, resourceResponse.body(), true));
                            break;
                        }
                        case StatusCode.createOrder:{
                            Response<BaseResponse> resourceResponse = syncService.createOrder((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.createOrder, resourceResponse.body(), true));
                            break;
                        }
                    }
                    //Analyze the final data
                } catch (Exception e) {
                    EventBus.getDefault().post(new ObjectEvent<>(StatusCode.networkError, null, false));
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
