package com.lvr.livecircle.recommend;

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
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
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
 * Created by lvr on 2017/2/6.
 */

public class MyResourceFragment extends BaseFragment {
    //当前页面数（第一次加载默认为1）
    private int mStartPage = 1;
    //每页元素个数（默认为10）
    private int maxNumber= 10;
    //绑定列表展示用的recycle
    @BindView(R.id.order_recycle)
    XRecyclerView xRecyclerView;
    //当未获取到数据时展示的页面
    @BindView(R.id.order_no_data)
    TextView no_data;

    //通用的适配器
    private CommonAdapter adapter;
    //返回资源容器
    private List<ResponseResource> results=new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        getData();
    }

    /**
     * 调用present来访问api以获取数据
     */
    private void getData(){
        if(Cache.getInstance().getUser()==null)
            return;
        mStartPage = 1;
        results.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter = null;
        }
        Resources resources=new Resources();
        resources.setPage(mStartPage);
        resources.setName(Cache.getInstance().getUser().getUser_name());
        RegisterPresent present=new RegisterPresentImpl();
        present.getMyResource(resources);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }



    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getMyResources: {
                stopProgressDialog();
                if (((BaseResponse)event.getObject()).getCode()==1) {
                    mStartPage++;
                    BaseResponse baseResponse= (BaseResponse) event.getObject();
//                    Gson gson = new Gson();
//                    String json = gson.toJson(baseResponse.getInfo());
//                    List<ResponseResource> responseResources=jsonToBeanList(json,ResponseResource.class);
                    initChangeRecycle((List<ResponseResource>) baseResponse.getInfo());
                } else {
                    showLongToast("拉取数据失败！");
                }
                break;
            }
        }
    }


    /**
     * 初始化列表
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(BallBeat);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setLoadingMoreEnabled(true);
        adapter = new CommonAdapter<ResponseResource>(getActivity(), R.layout.resources, results) {
            @Override
            protected void convert(ViewHolder holder, ResponseResource responseResource, int position) {
                Glide.with(getActivity()).load(responseResource.getImg1()).into((ImageView) holder.getView(R.id.resource_img));
                holder.setText(R.id.resource_name,responseResource.getName());
                holder.setText(R.id.resource_add_time, DateUtil.date2Str(new Date(Long.parseLong(responseResource.getAdd_time())),DateUtil.FORMAT_DEFAULT));
                holder.setText(R.id.resource_credit, responseResource.getCredit_number());
                holder.setText(R.id.resource_price,responseResource.getPricenew());
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
                if ( resources.size()<maxNumber) {
                    showShortToast( "没有更多了");
                    xRecyclerView.loadMoreComplete();
                    return;
                }
                Resources resources=new Resources();
                resources.setPage(mStartPage);
                resources.setName(Cache.getInstance().getUser().getUser_name());
                RegisterPresent present=new RegisterPresentImpl();
                present.getMyResource(resources);
            }
        });
    }

    /**
     * 为解决泛型传递集合时出现解析异常所使用的工具类
     * @param json
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToBeanList(String json, Class<T> t) {
        List<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray jsonarray = parser.parse(json).getAsJsonArray();
        for (JsonElement element : jsonarray
                ) {
            list.add(new Gson().fromJson(element, t));
        }
        return list;
    }
}
