package com.lvr.livecircle.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
import com.lvr.livecircle.bean.Order;
import com.lvr.livecircle.bean.Resources;
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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.jcodecraeer.xrecyclerview.ProgressStyle.BallBeat;
import static com.jcodecraeer.xrecyclerview.ProgressStyle.BallSpinFadeLoader;

/**
 * 我的买入订单页面
 */
public class OrderActivity extends BaseActivity implements DialogTwoButtonClickListener{
    //当前页面数（第一次加载默认为1）
    private int mStartPage = 1;
    //每页元素个数（默认为10）
    private int maxNumber = 10;
    //绑定列表展示用的recycle
    @BindView(R.id.order_recycle)
    XRecyclerView xRecyclerView;
    //当未获取到数据时展示的页面
    @BindView(R.id.order_no_data)
    TextView no_data;
    @BindView(R.id.top_title)
    TextView top_title;
    //通用的适配器
    private CommonAdapter adapter;
    //返回资源容器
    private List<Order> results = new ArrayList<>();

    private Order checkOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        top_title.setText("我买入的");
        getData();
    }


    /**
     * 调用present来访问api以获取数据
     */
    private void getData() {
        startProgressDialog();
        if (Cache.getInstance().getUser() == null)
            return;
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
        present.getMyOrderList(resources);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getMyOrderList: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    mStartPage++;
                    BaseResponse baseResponse = (BaseResponse) event.getObject();
                    initChangeRecycle((List<Order>) baseResponse.getInfo());
                } else {
                    showLongToast("拉取数据失败！");
                }
                break;
            }
            case StatusCode.deleteBuyOrder: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    showShortToast("订单取消成功！");
                  getData();
                } else {
                    showLongToast("操作订单失败！");
                }
                break;
            }
        }
    }


    @OnClick(R.id.top_back)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.top_back: {
                finish();
                break;
            }
        }
    }


    /**
     * 初始化列表
     *
     * @param resources
     */
    private void initChangeRecycle(final List<Order> resources) {
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
        adapter = new CommonAdapter<Order>(this, R.layout.order, results) {
            @Override
            protected void convert(ViewHolder holder, final Order order, int position) {
                Glide.with(OrderActivity.this).load(order.getResources_img1()).into((ImageView) holder.getView(R.id.order_img));
                holder.setText(R.id.order_number, order.getOrderno());
                holder.setText(R.id.order_time, DateUtil.date2Str(new Date(Long.parseLong(order.getItime())), DateUtil.FORMAT_DEFAULT));
                holder.setText(R.id.order_name, order.getResources_name());
                holder.setText(R.id.order_distribution, order.getShipping_status().equals("0") ? "未发货" : "已发货");
                holder.setText(R.id.order_price, order.getResources_price());
                holder.setVisible(R.id.order_sure, false);
                holder.setVisible(R.id.order_delete, false);
                switch (order.getOrder_status()) {
                    case "1": {
                        holder.setText(R.id.order_status, "进行中");
                        holder.setVisible(R.id.order_delete, true);
                        holder.setOnClickListener(R.id.order_delete, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                checkOrder=order;
                                DialogUtil.showCheckDialog(OrderActivity.this,"取消订单","订单取消将无法恢复，请确认是否要取消订单？","是的","我再想想",StatusCode.deleteBuyOrder,OrderActivity.this);
                            }
                        });
                        break;
                    }
                    case "2": {
                        holder.setText(R.id.order_status, "取消");
                        break;
                    }
                    case "3": {
                        holder.setText(R.id.order_status, "已完成");
                        break;
                    }
                }

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
                resources.setUser_id(Cache.getInstance().getUser().getUser_id());
                RegisterPresent present = new RegisterPresentImpl();
                present.getMyOrderList(resources);
            }
        });
    }


    @Override
    public void clickYes(int type) {
        switch(type){
            case StatusCode.deleteBuyOrder:{
                startProgressDialog();
                RegisterPresent registerPresent = new RegisterPresentImpl();
                Resources resources1 = new Resources();
                resources1.setId(checkOrder.getId());
                resources1.setUser_id(Cache.getInstance().getUser().getUser_id());
                registerPresent.deleteBuyOrder(resources1);
            }
        }
    }

    @Override
    public void clickNo(int type) {

    }
}
