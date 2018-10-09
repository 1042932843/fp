package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.DuskyApp;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.dialog.DialogLoading;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyClearEditText;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.entity.User;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PgyUpdateManagerListener;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LoginActivity extends BaseActivity implements KeyClearEditText.KeyPreImeListener{
    DialogLoading dialogLoading;
    @BindView(R.id.phone)
    KeyClearEditText phone;
    @BindView(R.id.password)
    KeyClearEditText password;

    int dev=0;
    @OnClick(R.id.titleimg)
    public void dev(){
        dev++;
        if(dev>=8){
            //ToastUtil.showShort(this,"进入开发者模式");
        }
    }

    @OnClick(R.id.login)
    public void doLogin(){
        login();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        phone.setKeyPreImeListener(this);
        password.setKeyPreImeListener(this);
        String s= PreferenceUtil.getStringPRIVATE("id", UserState.NA);
        String pt= PreferenceUtil.getString("username", "");
        if(!TextUtils.isEmpty(pt)){
            phone.setText(pt);
        }
        //LogUtil.d(s);
        if(s.isEmpty()||UserState.NA.equals(s)){
            update();
        }else{
            String role= PreferenceUtil.getStringPRIVATE("role", UserState.NA);
            if(!UserState.NA.equals(role)&&!"".equals(role)){
                if("2".equals(role)){
                    startActivity(new Intent(LoginActivity.this,Main2Activity.class));
                }else if("1".equals(role)){
                    startActivity(new Intent(LoginActivity.this,MapActivity.class));
                }
                finish();
            }else{
                ToastUtil.showShort(LoginActivity.this,"您的帐号没有相关权限，请尝试重新登录或者联系管理员");
            }
        }


    }

    private void update() {
        try

        {
            PgyUpdateManager.register(this, new PgyUpdateManagerListener(this));
        } catch (
                Exception e
                )
        {
            PgyCrashManager.reportCaughtException(this, e);
            ToastUtil.ShortToast("检查更新失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
    }

    @Override
    public void initToolBar() {

    }

    String phoneText;
    // 调用登录
    @SuppressLint("CheckResult")
    private void login() {
       phoneText = phone.getText().toString();
        if (TextUtils.isEmpty(phoneText)) {
            ToastUtil.ShortToast("请输入用户名");
            return;
        }

        String passwordText = password.getText().toString();
        if (TextUtils.isEmpty(passwordText)) {
            ToastUtil.ShortToast("请输入密码");
            return;
        }

        dialogLoading=new DialogLoading();
        dialogLoading.setMessage("提交中");
        dialogLoading.show(getFragmentManager(),DialogLoading.TAG);
        RetrofitHelper.getLoginRegisterAPI()
                .login(phoneText,passwordText)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            User user= JSON.parseObject(apiMsg.getResult(),User.class);
                            //Object obj1 = JSON.parseObject(apiMsg.getResult(),Object.class);
                           //String ob= obj1.toString();
                            if(user!=null){
                                PreferenceUtil.put("username",phoneText);
                                PreferenceUtil.putStringPRIVATE("id",user.getId());
                                PreferenceUtil.putStringPRIVATE("account",user.getAccount());
                                PreferenceUtil.putStringPRIVATE("password",user.getPassword());
                                PreferenceUtil.putStringPRIVATE("name",user.getName());
                                PreferenceUtil.putStringPRIVATE("phone",user.getPhone());
                                PreferenceUtil.putStringPRIVATE("sex",user.getSex());
                                PreferenceUtil.putStringPRIVATE("idcard",user.getIdcard());
                                PreferenceUtil.putStringPRIVATE("email",user.getEmail());
                                PreferenceUtil.putStringPRIVATE("role",user.getRole());
                                PreferenceUtil.putStringPRIVATE("picture",user.getPicture());
                                PreferenceUtil.putStringPRIVATE("permissions",user.getPermissions());
                                PreferenceUtil.putStringPRIVATE("villagename",user.getCurrentname());
                                PreferenceUtil.putStringPRIVATE("townid",user.getParentid());
                                PreferenceUtil.putStringPRIVATE("town",user.getParentname());
                                Set<String> tags=new HashSet<String>();
                                tags.add(user.getPermissions());
                                //JPushInterface.setTags(this,1042932843,tags);
                                if(!UserState.NA.equals(user.getRole())&&!"".equals(user.getRole())){
                                    if("2".equals(user.getRole())){
                                        startActivity(new Intent(LoginActivity.this,Main2Activity.class));
                                    }else if("1".equals(user.getRole())){
                                        startActivity(new Intent(LoginActivity.this,MapActivity.class));
                                    }
                                    finish();
                                }else{
                                    ToastUtil.showShort(LoginActivity.this,"您的帐号没有相关权限，请尝试重新登录或者联系管理员");
                                }

                            }
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


    private long onBackPressedTimeMillis; // 按下返回键的时间戳
    @Override
    public void onBackPressed() { // 连续按下两次返回键才退出App
        long currentTimeMillis = System.currentTimeMillis();
        if (onBackPressedTimeMillis != 0 && currentTimeMillis - onBackPressedTimeMillis < 3000) {
            DuskyApp.getInstance().Exit();
            super.onBackPressed();
        } else {
            ToastUtil.LongToast("再按一次返回键退出");

        }
        onBackPressedTimeMillis = currentTimeMillis;
    }

    @Override
    public void onKeyPreImeUp(int keyCode, KeyEvent event) {
        phone.clearFocus();
        password.clearFocus();
    }
}
