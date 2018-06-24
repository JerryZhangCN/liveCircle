package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.lvr.livecircle.R;
import com.lvr.livecircle.api.ImgUpload;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.User;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.DateUtil;
import com.lvr.livecircle.utils.GlideCircleTransform;
import com.lvr.livecircle.utils.GlideImageLoader;
import com.lvr.livecircle.utils.StatusCode;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class UserMessageActivity extends BaseActivity {

    @BindView(R.id.user_nick_name)
    EditText userName;
    @BindView(R.id.user_email)
    EditText userEmail;
    @BindView(R.id.chose_user_pic)
    ImageView chose_user_pic;
    @BindView(R.id.top_title)
    TextView top_title;
    @BindView(R.id.top_done)
    TextView top_down;
    @BindView(R.id.user_brith)
    TextView user_bruth;

    private ImagePicker imagePicker;
    private String imagePath;
    Calendar startDate = Calendar.getInstance();
    private Date brith = new Date();
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_message;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        top_title.setText("我的个人信息");
        top_down.setText("完成");
        top_down.setVisibility(View.VISIBLE);
        userName.setText(Cache.getInstance().getUser().getUser_name());
        userEmail.setText(Cache.getInstance().getUser().getEmali());
        user_bruth.setText(Cache.getInstance().getUser().getBirth());
        Glide.with(UserMessageActivity.this).load(Cache.getInstance().getUser().getHead_img()).transform(new GlideCircleTransform(UserMessageActivity.this)).into((chose_user_pic));
    }

    @OnClick({R.id.chose_user_pic, R.id.top_done, R.id.top_back, R.id.user_brith})
    public void clickEvents(View v) {
        switch (v.getId()) {
            case R.id.chose_user_pic: {
                imgpickerSetting();
                startActivityForResult(new Intent(UserMessageActivity.this, ImageGridActivity.class), StatusCode.getImg);
                break;
            }
            case R.id.top_done: {
                if (!checkParams()) {
                    showShortToast("请输入完整信息");
                    return;
                }
                startProgressDialog();
                if (imagePath == null) {
                    imagePath = Cache.getInstance().getUser().getHead_img();
                } else
                    imagePath = ImgUpload.upload(imagePath);
                Log.d("上传图片成功", imagePath);
                Glide.with(UserMessageActivity.this).load(imagePath).transform(new GlideCircleTransform(UserMessageActivity.this)).into((chose_user_pic));
                user = Cache.getInstance().getUser();
                user.setHead_img(imagePath);
                user.setEmali(userEmail.getText().toString());
                user.setUser_name(userName.getText().toString());
                user.setBirth(user_bruth.getText().toString());
                RegisterPresent present = new RegisterPresentImpl();
                present.updateUserMsg(user);
                break;
            }
            case R.id.top_back: {
                finish();
                break;
            }
            case R.id.user_brith: {
                initCalender(brith, user_bruth);
                break;
            }
            default:
                break;
        }
    }

    //注册返回结果的消息接受
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.updateUserMsg: {
                stopProgressDialog();
                if (((BaseResponse) event.getObject()).getCode() == 1) {
                    showLongToast("修改用户信息成功！");
                    Cache.getInstance().setUser(user);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showLongToast("注册失败！");
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StatusCode.getImg: {
                if (data != null && requestCode == StatusCode.getImg) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    glideResult(images);
                }
                break;
            }
        }
    }

    //处理imgpicker返回数据
    private void glideResult(ArrayList<ImageItem> images) {
        ImageView imageView;
        imageView = new ImageView(UserMessageActivity.this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 280);
        imageView.setLayoutParams(params);
        imageView.setBackgroundColor(Color.parseColor("#88888888"));
        imagePath = images.get(0).path;
        imagePicker.getImageLoader().displayImage(UserMessageActivity.this, images.get(0).path, chose_user_pic, 280, 280);
    }


    //imgpiker属性设置
    private void imgpickerSetting() {
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);//允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(256);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(256);//保存文件的高度。单位像素
        imagePicker.setStyle(CropImageView.Style.CIRCLE);
        imagePicker.setMultiMode(false);
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
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true)
                .setOutSideCancelable(false)
                .build();
        pvTime.show();
    }

    public boolean checkParams() {
        if (userName.getText().toString() == null || userName.getText().toString() == null || userEmail.getText().toString() == null || userEmail.getText().toString() == null)
            return false;
        return true;
    }
}
