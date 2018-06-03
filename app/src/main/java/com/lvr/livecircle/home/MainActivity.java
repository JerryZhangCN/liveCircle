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
    private String[] mMoudleName = {"精品推荐", "我发布的", "我买入的", "我卖出的"};
    private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    private RecommendFragment mRecommendFragment;
    private OrderFragment mOrderFragment;
    private MyResourceFragment mMyResourceFragment;
    private ShellFragment mShellFragment;
    private TextView user_name;
    private TextView user_credit;
    private MenuItem mn_phone;
    private MenuItem mn_sex;
    private MenuItem mn_brith;
    private MenuItem mn_email;
    private MenuItem mn_out;
    private MenuItem mn_hint;
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
        if (mMyResourceFragment.isAdded()) {
            manager.putFragment(outState, "MyResourceFragment", mMyResourceFragment);
        }
        if (mOrderFragment.isAdded()) {
            manager.putFragment(outState, "OrderFragment", mOrderFragment);
        }
        if (mShellFragment.isAdded()) {
            manager.putFragment(outState, "ShellFragment", mShellFragment);
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
            mOrderFragment = (OrderFragment) getSupportFragmentManager().getFragment(savedInstanceState, "OrderFragment");
            mMyResourceFragment = (MyResourceFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MyResourceFragment");
            mShellFragment = (ShellFragment) getSupportFragmentManager().getFragment(savedInstanceState, "ShellFragment");
        } else {
            mRecommendFragment = new RecommendFragment();
            mOrderFragment = new OrderFragment();
            mMyResourceFragment = new MyResourceFragment();
            mShellFragment = new ShellFragment();
        }
        mFragmentList.add(mRecommendFragment);
        mFragmentList.add(mMyResourceFragment);
        mFragmentList.add(mOrderFragment);
        mFragmentList.add(mShellFragment);

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
        mn_phone = footView.getItem(0);
        mn_sex = footView.getItem(1);
        mn_brith = footView.getItem(2);
        mn_email = footView.getItem(3);
        mn_out = footView.getItem(4);
        mn_hint = footView.getItem(5);
        if (isLogin) {
            mn_sex.setTitle(Cache.getInstance().getUser().getSex() == 1 ? "男" : "女");
            mn_brith.setTitle(Cache.getInstance().getUser().getBirth());
            mn_phone.setTitle(Cache.getInstance().getUser().getAddress());
            mn_email.setTitle(Cache.getInstance().getUser().getEmali());
            mn_out.setVisible(true);
            mn_sex.setVisible(true);
            mn_brith.setVisible(true);
            mn_phone.setVisible(true);
            mn_email.setVisible(true);
            mn_hint.setVisible(false);

        }
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
