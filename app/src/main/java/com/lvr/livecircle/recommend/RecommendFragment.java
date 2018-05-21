package com.lvr.livecircle.recommend;

import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseFragment;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.utils.StatusCode;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by lvr on 2017/2/6.
 */

public class RecommendFragment extends BaseFragment {
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recommend;
    }



    @Override
    protected void initView() {

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getResources: {
                stopProgressDialog();
                if (((BaseResponse)event.getObject()).getCode()==1) {
                } else {
                    showLongToast("拉取数据失败！");
                }
                break;
            }
        }
    }
}
