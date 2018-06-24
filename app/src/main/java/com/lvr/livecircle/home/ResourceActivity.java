package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.DateUtil;
import com.lvr.livecircle.utils.StatusCode;

import java.util.Date;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class ResourceActivity extends BaseActivity {
    @BindView(R.id.tv_img)
    ImageView resource_img;
    @BindView(R.id.tv_img2)
    ImageView resource_img2;
    @BindView(R.id.tv_img3)
    ImageView resource_img3;
    @BindView(R.id.tv_name)
    TextView resource_name;
    @BindView(R.id.tv_hint)
    TextView resource_hint;
    @BindView(R.id.tv_time)
    TextView resource_time;
    @BindView(R.id.tv_price)
    TextView resource_price;
    @BindView(R.id.tv_credit)
    TextView resource_credit;
    @BindView(R.id.tv_user_name)
    TextView resource_user_name;
    @BindString(R.string.resource_credit)
    String credit;
    @BindString(R.string.resource_time)
    String time;
    @BindString(R.string.resource_price)
    String price;
    @BindString(R.string.resource_user_name)
    String user_name;
    @BindView(R.id.top_title)
    TextView top_title;


    private String resources_id = null;
    private ResponseResource responseResource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_resource);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getResourceById: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    initData((List<ResponseResource>) ((BaseResponse) event.getObject()).getInfo());
                } else {
                    showLongToast("拉取数据失败！");
                }
                break;
            }
            case StatusCode.createOrder: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    showLongToast("下单成功！");
                    finish();
                } else {
                    showLongToast(((BaseResponse) event.getObject()).getMsg());
                }
                break;
            }
            case StatusCode.collectionResource: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    showLongToast("收藏成功！");
                    finish();
                } else {
                    showLongToast(((BaseResponse) event.getObject()).getMsg());
                }
                break;
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_resource;
    }

    @Override
    public void initPresenter() {
        top_title.setText("资源详情");
    }

    @Override
    public void initView() {
        startProgressDialog();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Resources resources = new Resources();
        resources.setId(id);
        RegisterPresent present = new RegisterPresentImpl();
        present.getResourceById(resources);
    }

    @OnClick({R.id.button_create_order, R.id.button_collection_order, R.id.top_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_order: {
                if (Cache.getInstance().getUser() == null) {
                    showLongToast("您尚未登录，无法下单！");
                    return;
                }
                if (responseResource.getUser_name().equals(Cache.getInstance().getUser().getUser_name())) {
                    showLongToast("无法购买自己发布的资源！");
                    return;
                }
                Resources resources = new Resources();
                resources.setResources_id(resources_id);
                resources.setUser_id(Cache.getInstance().getUser().getUser_id());
                RegisterPresent present = new RegisterPresentImpl();
                present.createOrder(resources);
                break;
            }
            case R.id.button_collection_order: {
                if (Cache.getInstance().getUser() == null) {
                    showLongToast("您尚未登录，无法进行收藏操作！");
                    return;
                }
                if (responseResource.getUser_name().equals(Cache.getInstance().getUser().getUser_name())) {
                    showLongToast("无法收藏自己发布的资源！");
                    return;
                }
                Resources resources = new Resources();
                resources.setResources_id(resources_id);
                resources.setUser_id(Cache.getInstance().getUser().getUser_id());
                RegisterPresent present = new RegisterPresentImpl();
                present.collectionResource(resources);
                break;
            }
            case R.id.top_back: {
                finish();
                break;
            }
        }

    }

    private void initData(List<ResponseResource> responseResources) {
        if (responseResources.size() == 0 || responseResources == null)
            return;
        responseResource = responseResources.get(0);
        resources_id = responseResource.getId();
        if (responseResource.getImg1() != null) {
            resource_img.setVisibility(View.VISIBLE);
            Glide.with(this).load(responseResource.getImg1()).into(resource_img);
        }
        if (responseResource.getImg2() != null) {
            resource_img2.setVisibility(View.VISIBLE);
            Glide.with(this).load(responseResource.getImg2()).into(resource_img2);
        }
        if (responseResource.getImg3() != null) {
            resource_img3.setVisibility(View.VISIBLE);
            Glide.with(this).load(responseResource.getImg3()).into(resource_img3);
        }
        resource_name.setText(responseResource.getName());
        resource_hint.setText(responseResource.getCmt());
        resource_time.setText(time + DateUtil.date2Str(new Date(Long.parseLong(responseResource.getAdd_time())), DateUtil.FORMAT_DEFAULT));
        resource_price.setText(price + responseResource.getPricenew());
        resource_user_name.setText(user_name + responseResource.getUser_name());
        resource_credit.setText(credit + responseResource.getCredit_number());
    }
}
