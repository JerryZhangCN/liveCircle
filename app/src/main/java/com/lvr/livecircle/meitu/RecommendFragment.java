package com.lvr.livecircle.meitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
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
import com.lvr.livecircle.bean.ResourceType;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.bean.ResponseResource;
import com.lvr.livecircle.home.ResourceActivity;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.DateUtil;
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


public class RecommendFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    //当前页面数（第一次加载默认为1）
    private int mStartPage = 1;
    //每页元素个数（默认为10）
    private int maxNumber = 10;
    //绑定列表展示用的recycle
    @BindView(R.id.recycle)
    XRecyclerView xRecyclerView;
    //当未获取到数据时展示的页面
    @BindView(R.id.report_no_data)
    TextView no_data;
    @BindView(R.id.resource_type)
    Spinner resource_type;
    @BindView(R.id.search_ed)
    EditText editText;

    //通用的适配器
    private CommonAdapter adapter;
    //返回资源容器
    private List<ResponseResource> results = new ArrayList<>();

    private String type;
    private boolean isSearch = false;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recommend;
    }

    private List<String> resource_types = new ArrayList<>();

    @Override
    protected void initView() {
        getData();
    }

    /**
     * 调用present来访问api以获取数据
     */
    private void getData() {
        mStartPage = 1;
        results.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            adapter = null;
        }
        Resources resources = new Resources();
        resources.setPage(mStartPage);
        resources.setResources_type_id(type);
        resources.setName(editText.getText().toString());
        RegisterPresent present = new RegisterPresentImpl();
        present.getResource(resources);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    @OnClick(R.id.search)
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.search: {
                if (isSearch) {
                    getData();
                    editText.setVisibility(View.INVISIBLE);
                } else
                    editText.setVisibility(View.VISIBLE);
                isSearch = !isSearch;
                break;
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getResources: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    mStartPage++;
                    BaseResponse baseResponse = (BaseResponse) event.getObject();
                    Gson gson = new Gson();
                    String json = gson.toJson(baseResponse.getInfo());
                    List<ResponseResource> responseResources = jsonToBeanList(json, ResponseResource.class);
                    if (adapter != null) {
                        results.addAll(responseResources);
                        adapter.notifyDataSetChanged();
                        xRecyclerView.loadMoreComplete();
                    } else
                        initChangeRecycle(responseResources);
                } else {
                    showLongToast("拉取数据失败！");
                }
                break;
            }
            case StatusCode.getResourceType: {
                BaseResponse baseResponse = (BaseResponse) event.getObject();
                Cache.getInstance().setResourceTypes((List<ResourceType>) baseResponse.getInfo());
                resource_types.add("全部类型");
                for (ResourceType resourceType : Cache.getInstance().getResourceTypes()) {
                    resource_types.add(resourceType.getName());
                }
                //参数包括( 句柄， 下拉列表显示方式layout（这里采用系统自带的), 下拉列表中的文本id值， 待显示的字符串数组 )；
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, android.R.id.text1, resource_types);
                resource_type.setAdapter(adapter1);
                resource_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i == 0) {
                            type = null;
                            getData();
                        } else {
                            type = Cache.getInstance().getResourceTypes().get(i - 1).getId();
                        }
                        getData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setRefreshProgressStyle(BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(BallBeat);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setLoadingMoreEnabled(true);
        adapter = new CommonAdapter<ResponseResource>(getActivity(), R.layout.resources, results) {
            @Override
            protected void convert(ViewHolder holder, final ResponseResource responseResource, int position) {
                Glide.with(getActivity()).load(responseResource.getImg1()).into((ImageView) holder.getView(R.id.resource_img));
                holder.setText(R.id.resource_name, responseResource.getName());
                holder.setText(R.id.resource_add_time, DateUtil.date2Str(new Date(Long.parseLong(responseResource.getAdd_time())), DateUtil.FORMAT_DEFAULT));
                holder.setText(R.id.resource_credit, responseResource.getCredit_number());
                holder.setText(R.id.resource_price, responseResource.getPricenew());
                holder.setOnClickListener(R.id.product_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ResourceActivity.class);
                        intent.putExtra("id", responseResource.getId());
                        startActivity(intent);
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
                resources.setResources_type_id(type);
                RegisterPresent present = new RegisterPresentImpl();
                present.getResource(resources);
            }
        });
    }

    /**
     * 为解决泛型传递集合时出现解析异常所使用的工具类
     *
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

