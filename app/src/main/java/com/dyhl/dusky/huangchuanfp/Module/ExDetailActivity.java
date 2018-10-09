package com.dyhl.dusky.huangchuanfp.Module;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.ImagePickerAdapter;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.SelectDialog;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.Exp;
import com.dyhl.dusky.huangchuanfp.Module.entity.SignDetail;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ExDetailActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener{

    protected ProgressDialog dialog;
    public static final int REQUEST_CODE_PREVIEW = 101;

    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;

    @BindView(R.id.publisher)
    TextView pulisher;
    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.publics)
    TextView publics;
    @OnClick(R.id.publics)
    public void setPublics(){

    }

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.context)
    TextView context;




    String num;
    String code;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exdetail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
        Intent it=getIntent();
        num=it.getStringExtra("expid");
        selImageList=new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, 0);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);


    }

    public void setPublic(Exp exp){
        List<String> names = new ArrayList<>();
        names.add("公开");
        names.add("屏蔽");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        RetrofitHelper.getExperienceServiceAPI().setupExperience(num,"公开").subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()).subscribe(bean -> {
                            String a=bean.string();
                            ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                            String state = apiMsg.getState();
                            switch (state){
                                case "0":
                                    exp.setPublics("公开");
                                    publics.setText(exp.getPublics());
                                    ToastUtil.ShortToast(apiMsg.getMessage());
                                    break;
                                case "-1":
                                case "-2":
                                    ToastUtil.ShortToast(apiMsg.getMessage());
                                    break;
                            }
                        }, throwable -> {
                            ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                        });

                        break;
                    case 1:
                        RetrofitHelper.getExperienceServiceAPI()
                                .setupExperience(num,"屏蔽")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                    String a=bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state){
                                        case "0":
                                            exp.setPublics("屏蔽");
                                            publics.setText(exp.getPublics());
                                            ToastUtil.ShortToast(apiMsg.getMessage());
                                            break;
                                        case "-1":
                                        case "-2":
                                            ToastUtil.ShortToast(apiMsg.getMessage());
                                            break;
                                    }
                                }, throwable -> {
                                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                                });
                        break;
                    default:
                        break;
                }

            }
        }, names);
    }
    SelectDialog sdialog;
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {

            sdialog = new SelectDialog(this, R.style
                    .transparentFrameWindowStyle,
                    listener, names);

            if (!this.isFinishing()) {
                sdialog.show();
            }

        return sdialog;
    }

    @Override
    public void initToolBar() {

    }


    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        dialog = new ProgressDialog(ExDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        dialog.show();
        RetrofitHelper.getExperienceServiceAPI()
                .selectExperienceDetail(num)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            Exp exp=JSON.parseObject(apiMsg.getResult(),Exp.class);
                            pulisher.setText(exp.getName());
                            Drawable drawable = getResources().getDrawable(R.drawable.ic_action_down);
                            publics.setText(exp.getPublics());
                            if(code.length()<=6){
                                publics.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                            }else{
                                if("公开".equals(exp.getPublics())){
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.pub);
                                    publics.setCompoundDrawablesWithIntrinsicBounds(drawable2,null,null,null);
                                }else{
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.pri);
                                    publics.setCompoundDrawablesWithIntrinsicBounds(drawable3,null,null,null);
                                }
                            }
                            publics.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(code.length()<=6){
                                        setPublic(exp);
                                    }else{
                                        //ToastUtil.ShortToast("您没有操作权限");
                                    }

                                }
                            });
                            title.setText(exp.getTitle());
                            title.getPaint().setFakeBoldText(true);
                            context.setText(exp.getValue());
                            time.setText(exp.getInputtime());
                            String pics=exp.getPicture();
                            if(pics!=null){
                                String[] array = pics.split(",");
                                for(int i=0;i<array.length;i++){
                                    if (!TextUtils.isEmpty(array[i])){
                                        ImageItem imageItem=new ImageItem();
                                        imageItem.path= ApiConstants.Base_URL+array[i];
                                        selImageList.add(imageItem);
                                    }
                                }
                                adapter.setMaxImgCount(selImageList.size());
                                adapter.setImages(selImageList);
                            }

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                    dialog.dismiss();
                }, throwable -> {
                    dialog.dismiss();
                    //tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });

    }


    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                intentPreview.putExtra(ImagePicker.EXTRA_CAN_DELETE, false);//设置不能点击删除，因为是作展示用的
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }


    private void initWidget() {
        setAppTitle("详情");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
