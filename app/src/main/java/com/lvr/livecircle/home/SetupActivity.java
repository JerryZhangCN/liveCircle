package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.bean.User;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.DateUtil;
import com.lvr.livecircle.utils.DialogTwoButtonClickListener;
import com.lvr.livecircle.utils.DialogUtil;
import com.lvr.livecircle.utils.StatusCode;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.jcodecraeer.xrecyclerview.ProgressStyle.BallBeat;
import static com.jcodecraeer.xrecyclerview.ProgressStyle.BallSpinFadeLoader;

/**
 * 我的资源页面
 */

public class SetupActivity extends BaseActivity implements DialogTwoButtonClickListener{
    //当前页面数（第一次加载默认为1）
    private int mStartPage = 1;
    //每页元素个数（默认为10）
    private int maxNumber = 10;
    //绑定列表展示用的recycle
    @BindView(R.id.my_source_recycle)
    XRecyclerView xRecyclerView;
    //当未获取到数据时展示的页面
    @BindView(R.id.my_resource_no_data)
    TextView no_data;
    @BindView(R.id.top_title)
    TextView top_title;

    //通用的适配器
    private CommonAdapter adapter;
    //返回资源容器
    private List<ResponseResource> results = new ArrayList<>();
    private ResponseResource checkResource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setup;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        top_title.setText("我的发布");
    }

    /**
     * 调用present来访问api以获取数据
     */
    private void getData() {
        if (Cache.getInstance().getUser() == null)
            return;
        startProgressDialog();
        mStartPage = 1;
        results.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter = null;
        }
        Resources resources = new Resources();
        resources.setPage(mStartPage);
        resources.setUser_id(Cache.getInstance().getUser().getUser_id());
        RegisterPresent present = new RegisterPresentImpl();
        present.getMyResource(resources);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.top_back, R.id.button_set_up})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back: {
                finish();
                break;
            }
            case R.id.button_set_up: {
                startActivity(CreateResourceActivity.class);
                break;
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getMyResources: {
                stopProgressDialog();
                xRecyclerView.loadMoreComplete();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    mStartPage++;
                    BaseResponse baseResponse = (BaseResponse) event.getObject();
                    if (adapter != null) {
                        results.addAll((List<ResponseResource>) baseResponse.getInfo());
                        adapter.notifyDataSetChanged();
                    } else
                        initChangeRecycle((List<ResponseResource>) baseResponse.getInfo());
                } else {
                    showLongToast("拉取数据失败！");
                }
                break;
            }
            case StatusCode.deleteResource: {
                stopProgressDialog();
                xRecyclerView.loadMoreComplete();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    showShortToast("删除资源成功！");
                    getData();
                } else {
                    showShortToast("删除资源失败！");
                }
                break;
            }
        }
    }


    /**
     * 初始化列表
     *
     * @param resources
     */
    private void initChangeRecycle(final List<ResponseResource> resources) {
        xRecyclerView.refreshComplete();
        results.addAll(resources);
        if (resources.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
            return;
        } else {
            no_data.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(BallBeat);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setLoadingMoreEnabled(true);
        adapter = new CommonAdapter<ResponseResource>(this, R.layout.resources, results) {
            @Override
            protected void convert(ViewHolder holder, final ResponseResource responseResource, int position) {
                Glide.with(SetupActivity.this).load(responseResource.getImg1()).into((ImageView) holder.getView(R.id.resource_img));
                holder.setText(R.id.resource_name, responseResource.getName());
                holder.setText(R.id.resource_add_time, DateUtil.date2Str(new Date(Long.parseLong(responseResource.getAdd_time())), DateUtil.FORMAT_DEFAULT));
                holder.setText(R.id.resource_credit, responseResource.getCredit_number());
                holder.setText(R.id.resource_price, responseResource.getPricenew());
                holder.setOnClickListener(R.id.resource_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkResource=responseResource;
                        DialogUtil.showCheckDialog(SetupActivity.this, "删除资源", "资源删除将无法恢复，请确认是否要删除资源？", "是的", "我再想想", StatusCode.deleteResource, SetupActivity.this);
                    }
                });
            }
};
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }

            @Override
            public void onLoadMore() {
                if (resources.size() < maxNumber) {
                    showShortToast("没有更多了");
                    xRecyclerView.loadMoreComplete();
                    return;
                }
                Resources resources = new Resources();
                resources.setPage(mStartPage);
                resources.setUser_id(Cache.getInstance().getUser().getUser_name());
                RegisterPresent present = new RegisterPresentImpl();
                present.getMyResource(resources);
            }
        });
    }

    @Override
    public void clickYes(int type) {
        switch (type) {
            case StatusCode.deleteResource: {
                startProgressDialog();
                RegisterPresent registerPresent = new RegisterPresentImpl();
                Resources resources1 = new Resources();
                resources1.setId(checkResource.getId());
                resources1.setUser_id(Cache.getInstance().getUser().getUser_id());
                registerPresent.deleteResource(resources1);
                break;
            }
        }
    }

    @Override
    public void clickNo(int type) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
