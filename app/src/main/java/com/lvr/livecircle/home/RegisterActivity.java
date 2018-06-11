package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.lvr.livecircle.R;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.User;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.DateUtil;
import com.lvr.livecircle.utils.StatusCode;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.top_done)
    public TextView login;
    @BindView(R.id.top_title)
    public TextView top_title;
    @BindView(R.id.ed_username)
    public EditText ed_userName;
    @BindView(R.id.ed_email)
    public EditText ed_email;
    @BindView(R.id.ed_password)
    public EditText ed_password;
    @BindView(R.id.ed_surepsd)
    public EditText ed_surepsd;
    @BindView(R.id.text_brith)
    public TextView text_brith;
    @BindView(R.id.ed_address)
    public EditText ed_address;
    @BindView(R.id.ed_id_card)
    public EditText ed_id_card;
    @BindView(R.id.sex)
    public Spinner spinner_sex;
    @BindView(R.id.ed_real_name)
    public EditText ed_real_name;

    Calendar startDate=Calendar.getInstance();
    private Date brith = new Date();


    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        initTopBar();

    }

    /**
     * 点击事件
     * @param view
     */
    @OnClick({R.id.top_back,R.id.top_done,R.id.btn_register,R.id.text_brith})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.top_back:{
                finish();
                break;
            }
            case R.id.top_done:{
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_register:{
                //检查参数
                if(!checkParams().equals("true")){
                Toast.makeText(this,checkParams(), Toast.LENGTH_SHORT).show();
                return;
                }
                User user=new User();
                user.setUser_name(ed_userName.getText().toString().trim());
                user.setEmali(ed_email.getText().toString().trim());
                user.setUser_password(ed_password.getText().toString().trim());
                user.setSex(((String)spinner_sex.getSelectedItem()).equals("男")?1:0);
                user.setBirth(text_brith.getText().toString());
                user.setAddress(ed_address.getText().toString());
                user.setId_card(ed_id_card.getText().toString());
                user.setRealname(ed_real_name.getText().toString());
                RegisterPresent present=new RegisterPresentImpl();
                present.register(user);
                break;
            }
            case R.id.text_brith:{
               initCalender(brith,text_brith);
                break;
            }
        }
    }

    //注册返回结果的消息接受
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.register: {
                stopProgressDialog();
                if (((BaseResponse)event.getObject()).getCode()==1) {
                    showLongToast("注册成功！");
                    Intent intent=new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showLongToast("注册失败！");
                }
                break;
            }
        }
    }

    private void initCalender(final Date input, final TextView textView) {
        Calendar endDate = Calendar.getInstance();
        startDate.set(1950, 0, 1);
        endDate.setTime(new Date());
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                textView.setText(DateUtil.date2Str(date, DateUtil.FORMAT_BRITH));
                input.setTime(date.getTime());
//                        selectDate.setTime(date);
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setCancelText("取消")
                .setSubmitText("确定")
                .setDate(endDate)
                .setRangDate(startDate, endDate)
                .isCyclic(true)
                .setLabel("年", "月", "日","时","分","秒")
                .isCenterLabel(true)
                .setOutSideCancelable(false)
                .build();
        pvTime.show();
    }




     //检查参数是否都已经输入
    private String checkParams(){
        if(ed_userName.getText().toString()==null||ed_email.getText().toString()==null||ed_password.getText().toString()==null||ed_surepsd.getText().toString()==null||ed_address.getText().toString()==null||ed_id_card.getText().toString()==null||text_brith.getText().toString()==null||ed_real_name.getText().toString()==null){
            return "请填入必要信息！";
        }else if(!ed_surepsd.getText().toString().equals(ed_password.getText().toString()))
            return "两次输入的密码不一致";
        return "true";
    }

    private void initTopBar(){
        top_title.setText(R.string.register);
        login.setText(R.string.login);
    }
}
