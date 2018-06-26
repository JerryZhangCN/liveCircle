package com.lvr.livecircle.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseFragment;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.Notice;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Order;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.home.NoticeCommentActivity;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.DateUtil;
import com.lvr.livecircle.utils.StatusCode;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.jcodecraeer.xrecyclerview.ProgressStyle.BallBeat;
import static com.jcodecraeer.xrecyclerview.ProgressStyle.BallSpinFadeLoader;

/**
 * 通告列表
 */

public class OrderFragment extends BaseFragment {
    //绑定列表展示用的recycle
    @BindView(R.id.my_source_recycle)
    XRecyclerView xRecyclerView;
    //当未获取到数据时展示的页面
    @BindView(R.id.my_resource_no_data)
    TextView no_data;

    //通用的适配器
    private CommonAdapter adapter;
    //返回资源容器
    private List<Notice> results = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_resource;
    }

    @Override
    protected void initView() {
        getData();
    }

    /**
     * 调用present来访问api以获取数据
     */
    private void getData() {
        results.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter = null;
        }
        RegisterPresent present = new RegisterPresentImpl();
        present.getNoticeList(new Resources());
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getNoticeList: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    BaseResponse baseResponse = (BaseResponse) event.getObject();
                    initChangeRecycle((List<Notice>) baseResponse.getInfo());
                } else {
                    showLongToast("拉取数据失败！");
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
    private void initChangeRecycle(final List<Notice> resources) {
        xRecyclerView.refreshComplete();
        results.addAll(resources);
        if (resources.size() == 0) {
            no_data.setVisibility(View.VISIBLE);
            return;
        } else {
            no_data.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(BallBeat);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setLoadingMoreEnabled(false);
        adapter = new CommonAdapter<Notice>(getActivity(), R.layout.notice_item, results) {
            @Override
            protected void convert(ViewHolder holder, final Notice notice, int position) {
                holder.setText(R.id.notice_title,notice.getTitle());
                holder.setText(R.id.notice_content,notice.getContent());
                holder.setText(R.id.notice_user,notice.getMemo());
                holder.setOnClickListener(R.id.notice_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(), NoticeCommentActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("notice",notice);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getData();
            }
            @Override
            public void onLoadMore() {
            }
        });
        xRecyclerView.setAdapter(adapter);
    }

}
