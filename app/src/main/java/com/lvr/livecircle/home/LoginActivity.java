package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.User;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.StatusCode;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.ed_login_name)
    public EditText ed_loginName;
    @BindView(R.id.ed_password)
    public EditText ed_password;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    @OnClick({R.id.top_back,R.id.top_done,R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_back:{
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.top_done:{
                Intent intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_login:{
                if(ed_password.getText().toString()==null||ed_loginName.getText().toString()==null)
                {Toast.makeText(this,"请输入账号密码！", Toast.LENGTH_SHORT).show();
                return;}
                User user=new User();
                user.setUser_name(ed_loginName.getText().toString().trim());
                user.setUser_password(ed_password.getText().toString().trim());
                RegisterPresent present=new RegisterPresentImpl();
                present.login(user);
                break;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.login: {
                stopProgressDialog();
                if (((BaseResponse)event.getObject()).getCode()==1) {
                    showLongToast("登录成功！");
                    Intent intent=new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showLongToast("登录失败！");
                }
                break;
            }
        }
    }
}
