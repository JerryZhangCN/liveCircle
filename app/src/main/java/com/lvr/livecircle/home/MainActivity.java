package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lvr.livecircle.R;
import com.lvr.livecircle.adapter.HomeViewPagerAdapter;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.Order;
import com.lvr.livecircle.meitu.RecommendFragment;
import com.lvr.livecircle.music.OrderFragment;
import com.lvr.livecircle.music.ShellFragment;
import com.lvr.livecircle.recommend.MyResourceFragment;
import com.lvr.livecircle.utils.StatusBarSetting;


import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView mAmNv;
    @BindView(R.id.drawer_layout)
    DrawerLayout mAmDl;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.vp_moudle)
    ViewPager mVpMoudle;
    private View mView_nav;
    private ImageView mIv_photo;
    private String[] mMoudleName = {"精品推荐", "小区布告"};
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private RecommendFragment mRecommendFragment;
    private OrderFragment mNoticeFragment;
    private TextView user_name;
    private TextView user_credit;
    private MenuItem mn_msg;
    private MenuItem mn_setup;
    private MenuItem mn_order;
    private MenuItem mn_shell;
    private MenuItem mn_out;
    private TextView tv_login;
    @BindString(R.string.credit)
    String credit;

    private HomeViewPagerAdapter mAdapter;
    private boolean isLogin = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (Cache.getInstance().getUser() != null)
            isLogin = true;
        StatusBarSetting.setColorForDrawerLayout(this, mAmDl, getResources().getColor(R.color.colorPrimary), StatusBarSetting.DEFAULT_STATUS_BAR_ALPHA);
        setToolBar();
        setNavigationView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化frament
        initFragment(savedInstanceState);
        setViewPager();
    }

    /**
     * 填充ViewPager内容
     */
    private void setViewPager() {
        mAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), mFragmentList, Arrays.asList(mMoudleName));
        mVpMoudle.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mVpMoudle);
        mTabs.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.black));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getSupportFragmentManager();
        if (mRecommendFragment.isAdded()) {
            manager.putFragment(outState, "RecommendFragment", mRecommendFragment);
        }

        if (mNoticeFragment.isAdded()) {
            manager.putFragment(outState, "OrderFragment", mNoticeFragment);
        }
    }

    /**
     * 初始化fragment的记忆状态
     *
     * @param savedInstanceState
     */
    private void initFragment(Bundle savedInstanceState) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState != null) {
            mRecommendFragment = (RecommendFragment) getSupportFragmentManager().getFragment(savedInstanceState, "RecommendFragment");
            mNoticeFragment = (OrderFragment) getSupportFragmentManager().getFragment(savedInstanceState, "OrderFragment");
        } else {
            mRecommendFragment = new RecommendFragment();
            mNoticeFragment = new OrderFragment();
        }
        mFragmentList.add(mRecommendFragment);
        mFragmentList.add(mNoticeFragment);

    }

    /**
     * 设置导航页信息
     */
    private void setNavigationView() {
        //NavigationView初始化
        mAmNv.setItemIconTintList(null);
        View headerView = mAmNv.getHeaderView(0);
        mIv_photo = (ImageView) headerView.findViewById(R.id.iv_user_photo);
        user_name = (TextView) headerView.findViewById(R.id.user_name);
        user_credit = (TextView) headerView.findViewById(R.id.user_credit);
        tv_login = (TextView) headerView.findViewById(R.id.tv_login);
        if (isLogin) {
            if (Cache.getInstance().getUser().getHead_img() != null)
                Glide.with(this).load(Cache.getInstance().getUser().getHead_img()).into(mIv_photo);
            user_name.setText(Cache.getInstance().getUser().getRealname());
            user_credit.setText(credit + ":" + Cache.getInstance().getUser().getCredit());
            user_name.setVisibility(View.VISIBLE);
            user_credit.setVisibility(View.VISIBLE);
            tv_login.setVisibility(View.INVISIBLE);
        }
        mIv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Menu footView = mAmNv.getMenu();
        mn_msg = footView.getItem(0);
        mn_setup = footView.getItem(1);
        mn_order = footView.getItem(2);
        mn_shell = footView.getItem(3);
        mn_out = footView.getItem(4);
        mn_setup.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!isLogin) {
                    showLongToast("您尚未登录，请登录");
                    return false;
                }
                startActivity(SetupActivity.class);
                return true;
            }
        });
        mn_order.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!isLogin) {
                    showLongToast("您尚未登录，请登录");
                    return false;
                }
                startActivity(OrderActivity.class);
                return true;
            }
        });
        mn_shell.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!isLogin) {
                    showLongToast("您尚未登录，请登录");
                    return false;
                }
                startActivity(ShellActivity.class);
                return true;
            }
        });
        mn_out.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!isLogin) {
                    showLongToast("您尚未登录，请登录");
                    return false;
                }
                Cache.getInstance().setUser(null);
                startActivity(LoginActivity.class);
                return true;
            }
        });

    }


    /**
     * 设置状态栏
     */
    private void setToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        //菜单按钮可用
        actionBar.setHomeButtonEnabled(true);
        //回退按钮可用
        actionBar.setDisplayHomeAsUpEnabled(true);
        //将drawlayout与toolbar绑定在一起
        ActionBarDrawerToggle abdt = new ActionBarDrawerToggle(this, mAmDl, mToolbar, R.string.app_name, R.string.app_name);
        abdt.syncState();//初始化状态
        //设置drawlayout的监听事件 打开/关闭
        mAmDl.setDrawerListener(abdt);
        //actionbar中的内容进行初始化
        mToolbar.setTitle("生活圈");//设置标题
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        //检测用户是否已登录


    }

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                R.anim.fade_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolabr, menu);
        return true;
    }

}
