package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.Notice;
import com.lvr.livecircle.bean.NoticeComment;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.Order;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.DateUtil;
import com.lvr.livecircle.utils.GlideCircleTransform;
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

//通告详情页面
public class NoticeCommentActivity extends BaseActivity {
    @BindView(R.id.top_title)
    TextView top_title;
    @BindView(R.id.notice_title)
    TextView notice_title;
    @BindView(R.id.notice_content)
    TextView notice_content;
    @BindView(R.id.notice_user)
    TextView notice_user;
    @BindView(R.id.notice_comment_recycle)
    XRecyclerView xRecyclerView;
    @BindView(R.id.notice_comment_no_data)
    TextView no_data;
    @BindView(R.id.ed_comment)
    EditText ed_comment;

    //当前页面数（第一次加载默认为1）
    private int mStartPage = 1;
    //每页元素个数（默认为10）
    private int maxNumber = 10;
    //通用的适配器
    private CommonAdapter adapter;
    //返回资源容器
    private List<NoticeComment> results = new ArrayList<>();
    private Notice notice = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_comment;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        notice = (Notice) intent.getSerializableExtra("notice");
        top_title.setText("通告详情");
        notice_title.setText(notice.getTitle());
        notice_content.setText(notice.getContent());
        notice_user.setText(notice.getMemo());
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
        resources.setNotice_id(notice.getId());
        RegisterPresent present = new RegisterPresentImpl();
        present.getNoticeComment(resources);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.getNoticeComment: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    mStartPage++;
                    BaseResponse baseResponse = (BaseResponse) event.getObject();
                    initChangeRecycle((List<NoticeComment>) baseResponse.getInfo());
                } else {
                    showLongToast("拉取数据失败！");
                }
                break;
            }
            case StatusCode.getPushComment: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    ed_comment.setText("");
                    showLongToast("发表成功！");
                    getData();
                } else {
                    showLongToast("发表失败！");
                }
                break;
            }
        }
    }
    @OnClick({R.id.top_back,R.id.button_push_comment})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.top_back:{
                finish();
                break;
            }
            case R.id.button_push_comment:{
                if(Cache.getInstance().getUser()==null) {
                    showShortToast("请登录后进行评论！");
                    return;
                }
                if(ed_comment.getText().toString()==null||ed_comment.getText().toString().equals("")) {
                    showShortToast("请输入评论内容！");
                    return;
                }
                startProgressDialog();
                Resources resources = new Resources();
                resources.setNotice_id(notice.getId());
                resources.setUser_id(Cache.getInstance().getUser().getUser_id());
                resources.setContent(ed_comment.getText().toString());
                RegisterPresent present = new RegisterPresentImpl();
                present.pushNoticeComment(resources);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); imm.hideSoftInputFromWindow(ed_comment.getWindowToken(), 0);
                break;
            }
        }
    }


    /**
     * 初始化列表
     *
     * @param resources
     */
    private void initChangeRecycle(final List<NoticeComment> resources) {
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
        xRecyclerView.setLoadingMoreEnabled(false);
        adapter = new CommonAdapter<NoticeComment>(this, R.layout.notice_comment, results) {
            @Override
            protected void convert(ViewHolder holder, NoticeComment noticeComment, int position) {
                Glide.with(NoticeCommentActivity.this).load(noticeComment.getHead_img()).transform(new GlideCircleTransform(NoticeCommentActivity.this)).into((ImageView) holder.getView(R.id.comment_img));
//                holder.setText(R.id.comment_msg, noticeComment.getUser_name() + ": " + noticeComment.getContent() + "   " + DateUtil.date2Str(new Date(Long.parseLong(noticeComment.getAdd_time())), DateUtil.FORMAT_DEFAULT));
                holder.setText(R.id.comment_msg, noticeComment.getUser_name() + ": " + noticeComment.getContent());
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
}
