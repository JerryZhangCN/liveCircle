package com.lvr.livecircle.home.present;

import com.lvr.livecircle.api.ServiceClient;
import com.lvr.livecircle.api.SyncService;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Notice;
import com.lvr.livecircle.bean.NoticeComment;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Order;
import com.lvr.livecircle.bean.ResourceType;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.bean.STS;
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

    @Override
    public void getNoticeList(Resources resources) {
        doConnect(StatusCode.getNoticeList, resources);
    }

    @Override
    public void getNoticeComment(Resources resources) {
        doConnect(StatusCode.getNoticeComment, resources);
    }

    @Override
    public void pushNoticeComment(Resources resources) {
        doConnect(StatusCode.getPushComment, resources);
    }

    @Override
    public void getSTS(Resources resources) {
        doConnect(StatusCode.getSTS, resources);
    }

    @Override
    public void updateUserMsg(User user) {
        doConnect(StatusCode.updateUserMsg, user);
    }

    @Override
    public void getResourceType(Resources resources) {
        doConnect(StatusCode.getResourceType, resources);
    }

    @Override
    public void createResource(Resources resources) {
        doConnect(StatusCode.createResource, resources);
    }

    @Override
    public void getUserCollection(Resources resources) {
        doConnect(StatusCode.getCollectionResource, resources);
    }

    @Override
    public void collectionResource(Resources resources) {
        doConnect(StatusCode.collectionResource, resources);
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
                        case StatusCode.createOrder: {
                            Response<BaseResponse> resourceResponse = syncService.createOrder((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.createOrder, resourceResponse.body(), true));
                            break;
                        }

                        case StatusCode.getNoticeList: {
                            Response<BaseResponse<List<Notice>>> resourceResponse = syncService.getNoticeList((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getNoticeList, resourceResponse.body(), true));
                            break;
                        }

                        case StatusCode.getNoticeComment: {
                            Response<BaseResponse<List<NoticeComment>>> resourceResponse = syncService.getNoticeComment((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getNoticeComment, resourceResponse.body(), true));
                            break;
                        }
                        case StatusCode.getPushComment: {
                            Response<BaseResponse> resourceResponse = syncService.pushComment((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getPushComment, resourceResponse.body(), true));
                            break;
                        }

                        case StatusCode.getSTS: {
                            Response<STS> resourceResponse = syncService.getSTS((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getSTS, resourceResponse.body(), true));
                            break;
                        }
                        case StatusCode.updateUserMsg: {
                            Response<BaseResponse> resourceResponse = syncService.updateUserMsg((User) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.updateUserMsg, resourceResponse.body(), true));
                            break;
                        }
                        case StatusCode.getResourceType: {
                            Response<BaseResponse<List<ResourceType>>> resourceResponse = syncService.getResourceType((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getResourceType, resourceResponse.body(), true));
                            break;
                        }
                        case StatusCode.createResource: {
                            Response<BaseResponse> resourceResponse = syncService.createResource((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.createResource, resourceResponse.body(), true));
                            break;
                        }
                        case StatusCode.getCollectionResource: {
                            Response<BaseResponse<List<ResponseResource>>> resourceResponse = syncService.getCollectionResource((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getCollectionResource, resourceResponse.body(), true));
                            break;
                        }
                        case StatusCode.collectionResource: {
                            Response<BaseResponse> resourceResponse = syncService.collectionResource((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.collectionResource, resourceResponse.body(), true));
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
