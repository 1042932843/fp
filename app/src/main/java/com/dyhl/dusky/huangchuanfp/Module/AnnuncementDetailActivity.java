package com.dyhl.dusky.huangchuanfp.Module;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.SelectDialog;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.richeditor.RichEditor;


public class AnnuncementDetailActivity extends BaseActivity {

    protected ProgressDialog dialog;

    @BindView(R.id.editor)
    RichEditor mEditor;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.publics)
    TextView publics;
    @OnClick(R.id.publics)
    public void set(){
        if(code.length()<=6){
            setPublic();
        }else{
            //ToastUtil.ShortToast("您没有操作权限");
        }
    }

    String id;
    String code;

    @Override
    public int getLayoutId() {
        return R.layout.activity_annuncementdetail_html;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
        Intent it=getIntent();
        id=it.getStringExtra("id");
        code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        //初始化编辑高度
        //mEditor.setEditorHeight(200);
        //初始化字体大小
        //mEditor.setEditorFontSize(22);
        //初始化字体颜色
        //mEditor.setEditorFontColor(Color.BLACK);
        //mEditor.setEditorBackgroundColor(Color.BLUE);

        //初始化内边距
        //mEditor.setPadding(10, 10, 10, 10);
        //设置编辑框背景，可以是网络图片
        // mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        // mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        //设置默认显示语句
        //mEditor.setPlaceholder("Insert text here...");
        //设置编辑器是否可用
        mEditor.setInputEnabled(false);

        mEditor.getSettings().setUseWideViewPort(true);
        mEditor.getSettings().setLoadWithOverviewMode(true);
    }

    @Override
    public void initToolBar() {

    }
    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        dialog = new ProgressDialog(AnnuncementDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        dialog.show();
        RetrofitHelper.getAnuncementAPI()
                .getAnuncementDetail(id)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            Annuncement annuncement= JSON.parseObject(apiMsg.getResult(),Annuncement.class);
                            title.setText(annuncement.getTitle());
                            time.setText(annuncement.getInputtime());
                            if(!TextUtils.isEmpty(annuncement.getSource())){
                                from.setText("来源："+annuncement.getSource());
                            }
                            if(!TextUtils.isEmpty(annuncement.getPublics())){
                                publics.setText(" "+annuncement.getPublics());
                                if(code.length()<=6){
                                    Drawable drawable = getResources().getDrawable(R.drawable.ic_action_down);
                                    publics.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
                                }else{
                                    if("公开".equals(annuncement.getPublics())){
                                        Drawable drawable2 = getResources().getDrawable(R.drawable.pub);
                                        publics.setCompoundDrawablesWithIntrinsicBounds(drawable2,null,null,null);
                                    }else{
                                        Drawable drawable3 = getResources().getDrawable(R.drawable.pri);
                                        publics.setCompoundDrawablesWithIntrinsicBounds(drawable3,null,null,null);
                                    }
                                }


                            }
                            mEditor.setHtml(annuncement.getValue());
                            //tip.setHint("");
                            break;
                        case "-1":
                        case "-2":
                            //tip.setHint("没有查询到相关信息");
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


    private void initWidget() {
        setAppTitle("详情");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }


    public void setPublic(){
        List<String> names = new ArrayList<>();
        names.add("公开");
        names.add("屏蔽");
        showDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long vid) {
                switch (position) {
                    case 0:
                        RetrofitHelper.getCommonServiceAPI()
                                .setupPublics(3,id,"公开")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                    String a=bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state){
                                        case "0":
                                            publics.setText("公开");
                                            Drawable drawable = getResources().getDrawable(R.drawable.pub);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);

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
                        RetrofitHelper.getCommonServiceAPI()
                                .setupPublics(3,id,"屏蔽")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean -> {
                                    String a=bean.string();
                                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                                    String state = apiMsg.getState();
                                    switch (state){
                                        case "0":
                                            publics.setText("屏蔽");
                                            Drawable drawable = getResources().getDrawable(R.drawable.pri);
                                            publics.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
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


    SelectDialog dialogs;
    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {

        dialogs = new SelectDialog((Activity) this, R.style
                .transparentFrameWindowStyle,
                listener, names);

        if (!this.isFinishing()) {
            dialogs.show();
        }

        return dialogs;
    }
}
