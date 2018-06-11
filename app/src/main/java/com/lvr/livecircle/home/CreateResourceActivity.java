package com.lvr.livecircle.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.lvr.livecircle.R;
import com.lvr.livecircle.api.ImgUpload;
import com.lvr.livecircle.base.BaseActivity;
import com.lvr.livecircle.bean.BaseResponse;
import com.lvr.livecircle.bean.Cache;
import com.lvr.livecircle.bean.ObjectEvent;
import com.lvr.livecircle.bean.ResourceType;
import com.lvr.livecircle.bean.Resources;
import com.lvr.livecircle.home.present.RegisterPresent;
import com.lvr.livecircle.home.present.RegisterPresentImpl;
import com.lvr.livecircle.utils.GlideImageLoader;
import com.lvr.livecircle.utils.StatusCode;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class CreateResourceActivity extends BaseActivity {
    @BindView(R.id.top_title)
    TextView top_title;
    @BindView(R.id.create_resource_name)
    EditText resource_name;
    @BindView(R.id.resource_img1)
    ImageView resource_img1;
    @BindView(R.id.resource_img2)
    ImageView resource_img2;
    @BindView(R.id.resource_img3)
    ImageView resource_img3;
    @BindView(R.id.resource_cmt)
    EditText resource_cmt;
    @BindView(R.id.resource_price)
    EditText resource_price;
    @BindView(R.id.resource_credit)
    EditText resource_credit;
    @BindView(R.id.resource_type)
    Spinner resource_type;

    private List<String> resource_types=new ArrayList<>();
    private Map<Integer,String> imagePaths=new HashMap<>();
    private ImagePicker imagePicker;
    private int spinnerId=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_resource;
    }

    @Override
    public void initPresenter() {
//       resource_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//           @Override
//           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//           }
//       });
    }

    @OnClick({R.id.resource_img1,R.id.resource_img2,R.id.resource_img3, R.id.button_create_order,R.id.top_back})
    public void clickEvents(View v) {
        switch (v.getId()) {
            case R.id.resource_img1: {
                imgpickerSetting();
                startActivityForResult(new Intent(CreateResourceActivity.this, ImageGridActivity.class), StatusCode.getImg1);
                break;
            }
            case R.id.resource_img2: {
                imgpickerSetting();
                startActivityForResult(new Intent(CreateResourceActivity.this, ImageGridActivity.class), StatusCode.getImg2);
                break;
            }
            case R.id.resource_img3: {
                imgpickerSetting();
                startActivityForResult(new Intent(CreateResourceActivity.this, ImageGridActivity.class), StatusCode.getImg3);
                break;
            }
            case R.id.button_create_order: {
                if (!checkParams()) {
                    showShortToast("请输入完整信息");
                    return;
                }
                if(imagePaths.size()>0)
                for(Integer s:imagePaths.keySet()){
                    imagePaths.put(s,ImgUpload.upload(imagePaths.get(s)));
                }
                Resources resources=new Resources();
                resources.setName(resource_name.getText().toString());
                resources.setCmt(resource_cmt.getText().toString());
                resources.setUser_id(Cache.getInstance().getUser().getUser_id());
                resources.setImg1(imagePaths.get(1));
                resources.setImg2(imagePaths.get(2));
                resources.setImg3(imagePaths.get(3));
                resources.setPrice(resource_price.getText().toString());
                resources.setCredit_number(resource_credit.getText().toString());
                resources.setResources_type_id(Cache.getInstance().getResourceTypes().get(resource_type.getSelectedItemPosition()).getId());
                RegisterPresent present = new RegisterPresentImpl();
                present.createResource(resources);
                break;
            }
            case R.id.top_back: {
                finish();
                break;
            }
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(ObjectEvent event) {
        switch (event.getType()) {
            case StatusCode.createResource: {
                stopProgressDialog();
                if (((BaseResponse)event.getObject()).getCode()==1) {
                    showLongToast("发布成功！");
                    finish();
                } else {
                    showLongToast("发布失败！");
                }
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case StatusCode.getImg1: {
                if (data != null && requestCode == StatusCode.getImg1) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    glideResult(images,resource_img1);
                }
                break;
            }
            case StatusCode.getImg2: {
                if (data != null && requestCode == StatusCode.getImg2) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    glideResult(images,resource_img2);
                }
                break;
            }
            case StatusCode.getImg3: {
                if (data != null && requestCode == StatusCode.getImg3) {
                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    glideResult(images,resource_img3);
                }
                break;
            }
        }
    }

    //处理imgpicker返回数据
    private void glideResult(ArrayList<ImageItem> images,ImageView view) {
        ImageView imageView;
        imageView = new ImageView(CreateResourceActivity.this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 280);
        imageView.setLayoutParams(params);
        imageView.setBackgroundColor(Color.parseColor("#88888888"));
        String imagePath = images.get(0).path;
        imagePicker.getImageLoader().displayImage(CreateResourceActivity.this, images.get(0).path, view, 280, 280);
        switch (view.getId()){
            case R.id.resource_img1:{
                imagePaths.put(1,imagePath);
                break;
            }
            case R.id.resource_img2:{
                imagePaths.put(2,imagePath);
                break;
            }
            case R.id.resource_img3:{
                imagePaths.put(3,imagePath);
                break;
            }
        }

    }

    //imgpiker属性设置
    private void imgpickerSetting() {
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);//允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(800);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(800);//保存文件的高度。单位像素
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        imagePicker.setMultiMode(false);
    }

    @Override
    public void initView() {
        for(ResourceType resourceType:Cache.getInstance().getResourceTypes()){
            resource_types.add(resourceType.getName());
        }
        //参数包括( 句柄， 下拉列表显示方式layout（这里采用系统自带的), 下拉列表中的文本id值， 待显示的字符串数组 )；
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1, resource_types);
        resource_type.setAdapter(adapter1);
    }

    public boolean checkParams() {
        if (resource_name.getText().toString() == null || resource_name.getText().toString() .equals("") || resource_cmt.getText().toString() == null || resource_cmt.getText().toString().equals("")|| resource_price.getText().toString() == null || resource_price.getText().toString() .equals("")|| resource_credit.getText().toString() == null || resource_credit.getText().toString() .equals(""))
            return false;
        return true;
    }
}
