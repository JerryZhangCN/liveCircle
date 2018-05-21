package com.lvr.livecircle.home.present;

import com.lvr.livecircle.api.ServiceClient;
import com.lvr.livecircle.api.SyncService;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.User;
import com.lvr.livecircle.utils.StatusCode;

import de.greenrobot.event.EventBus;
import retrofit2.Response;

/**
 * 与服务器交互的实现类
 */

public class RegisterPresentImpl implements RegisterPresent{
    @Override
    public void register(final User user) {
          doConnect(StatusCode.register,user);
    }

    @Override
    public void login(User user) {
        doConnect(StatusCode.login,user);
    }

    @Override
    public void getResource(Resources resources) {
        doConnect(StatusCode.getResources,resources);
    }


    /**
     * 网络请求
     * @param type
     * @param object
     */
    private void doConnect(final int type,final Object object){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SyncService syncService = ServiceClient.getService(SyncService.class);
                    Response<BaseResponse> response = null;
                    switch (type){
                        case StatusCode.register:{
                            response = syncService.register((User) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.register,response.body(),true));
                            break;
                        }
                        case StatusCode.login:{
                            response = syncService.login((User) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.login,response.body(),true));
                            break;
                        }
                        case StatusCode.getResources:{
                            response = syncService.getData((Resources) object).execute();
                            EventBus.getDefault().post(new ObjectEvent<>(StatusCode.getResources,response.body(),true));
                            break;
                        }
                    }
                    //Analyze the final data
                    System.out.print(response.body().toString());
                } catch (Exception e) {
                    EventBus.getDefault().post(new ObjectEvent<>(StatusCode.networkError,null,false));
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
