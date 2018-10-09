package com.dyhl.dusky.huangchuanfp.Base;


import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.dyhl.dusky.huangchuanfp.Utils.SystemBarHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Name: BaseActivity
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-06-25 21:20
 */
public abstract class BaseActivityForStatus extends RxAppCompatActivity {
  public static final String TAG = BaseActivityForStatus.class.getSimpleName();
  private Unbinder bind;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //设置布局内容
    setContentView(getLayoutId());
    //初始化黄油刀控件绑定框架
    bind = ButterKnife.bind(this);
    //初始化控件
    initViews(savedInstanceState);
    //初始化ToolBar
    initToolBar();
    loadData();
    initPermission();

  }


  @SuppressLint("CheckResult")
  private void initPermission() {
    RxPermissions rxPermissions = new RxPermissions(this);
    rxPermissions.setLogging(true);
    rxPermissions.requestEach(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE ,
            Manifest.permission.CAMERA,
            Manifest.permission.GET_TASKS,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.VIBRATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.REQUEST_INSTALL_PACKAGES

    ).subscribe(permission -> {
      if (permission.granted) {
        // 用户已经同意该权限
        Log.d(TAG, permission.name + " is granted.");
      } else if (permission.shouldShowRequestPermissionRationale) {
        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
        Log.d(TAG, permission.name + " is denied. More info should be provided.");
      } else {
        // 用户拒绝了该权限，并且选中『不再询问』
        Log.d(TAG, permission.name + " is denied.");
      }
    });
  }

  public abstract int getLayoutId();

  protected abstract void initViews(Bundle savedInstanceState);

  public abstract void initToolBar();


  public void loadData() {}


  @Override
  protected void onDestroy() {
    super.onDestroy();
    bind.unbind();
    EventBus.getDefault().unregister(this);
  }
}
