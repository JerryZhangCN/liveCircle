package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.User;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.StatusCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ed_login_name)
    public EditText ed_loginName;
    @BindView(R.id.ed_password)
    public EditText ed_password;
    @BindView(R.id.top_done)
    TextView top_down;


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
       top_down.setVisibility(View.VISIBLE);
        ed_password.setTypeface(Typeface.DEFAULT);
        ed_password.setTransformationMethod(new PasswordTransformationMethod());
        setEditTextInhibitInputSpeChat(ed_password);
        setEditTextInhibitInputSpeChat(ed_loginName);
    }

    @OnClick({R.id.top_back, R.id.top_done, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.top_done: {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_login: {
                if (ed_password.getText().toString() == null || ed_loginName.getText().toString() == null) {
                    Toast.makeText(this, "请输入账号密码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ed_password.getText().toString().length()<6){
                    showShortToast("密码长度不能小于6位");
                    return;
                }

                startProgressDialog();
                User user = new User();
                user.setUser_name(ed_loginName.getText().toString().trim());
                user.setUser_password(ed_password.getText().toString().trim());
                RegisterPresent present = new RegisterPresentImpl();
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
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    showLongToast("登录成功！");
                    BaseResponse<User> baseResponse = (BaseResponse<User>) event.getObject();
                    Cache.getInstance().setUser((baseResponse.getInfo()));
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showLongToast("登录失败！");
                }
                break;
            }
        }
    }
    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\]. <>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }
}
