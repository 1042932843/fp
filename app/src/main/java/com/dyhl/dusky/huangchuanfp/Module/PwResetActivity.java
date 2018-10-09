package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.DuskyApp;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.dialog.DialogLoading;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.ExMineFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.User;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PwResetActivity extends BaseActivity {

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }
    DialogLoading dialogLoading;
    @BindView(R.id.txt_title)
    TextView apptitle;
    @BindView(R.id.txt_right)
    TextView txt_right;

    @BindView(R.id.tip)
    TextView tip;

    @OnClick(R.id.txt_right)
    public void reset(){
        tip.setText("");
        String old=pwo.getText().toString();
        String newpw=pw.getText().toString();
        String newpws=pws.getText().toString();
        if(TextUtils.isEmpty(old)){
            tip.setText("请输入旧密码");
            return;
        }
        if(TextUtils.isEmpty(newpw)){
            tip.setText("请输入新密码");
            return;
        }
        if(TextUtils.isEmpty(newpws)){
            tip.setText("请重复密码加以确认");
            return;
        }
        if(!newpw.equals(newpws)){
            tip.setText("新密码两次输入不一致");
            return;
        }
        doReset(old,newpw);

    }

    public void goLogin(){
        Intent it=new Intent(this,LoginActivity.class);
        startActivity(it);
        finish();
    }

    @SuppressLint("CheckResult")
    private void doReset(String po, String pn) {
        String s= PreferenceUtil.getStringPRIVATE("id", UserState.NA);
        if(TextUtils.isEmpty(s)){
            ToastUtil.ShortToast("登录状态错误请重新登录");
            goLogin();
            return;
        }
        dialogLoading=new DialogLoading();
        dialogLoading.setMessage("提交中");
        dialogLoading.show(getFragmentManager(),DialogLoading.TAG);
        RetrofitHelper.getLoginRegisterAPI()
                .reset(s,po,pn)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            ToastUtil.ShortToast("修改成功");
                            DuskyApp.getInstance().logout();
                            goLogin();
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                    dialogLoading.dismiss();
                }, throwable -> {
                    dialogLoading.dismiss();
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }


    @BindView(R.id.pwo)
    KeyEditText pwo;
    @BindView(R.id.pw)
    KeyEditText pw;
    @BindView(R.id.pws)
    KeyEditText pws;


    @Override
    public int getLayoutId() {
        return R.layout.activity_resetpw;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
    }

    @Override
    public void initToolBar() {

    }



    @SuppressLint("CheckResult")
    public void loadData() {

    }

    private void initWidget() {
        setAppTitle("修改密码");
        txt_right.setText("完成");
        txt_right.setTextColor(getResources().getColor(R.color.colorPrimary));
        txt_right.setVisibility(View.VISIBLE);
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }


}


