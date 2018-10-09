package com.dyhl.dusky.huangchuanfp.Base;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.GlideImageLoader;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.Crash.CrashHandler;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.dyhl.dusky.huangchuanfp.Utils.service.LocationService;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.pgyersdk.crash.PgyCrashManager;
import com.zxy.tiny.Tiny;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;


/**
 * Name: DuskyApp
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2018-05-03 10:00
 */

public class DuskyApp extends MultiDexApplication implements Application.ActivityLifecycleCallbacks{
    public static DuskyApp mInstance;
    public static RequestOptions optionsRoundedCorners,optionsRoundedCircle;
    CrashHandler crashHandler= CrashHandler.getInstance();
    private ArrayList<Activity> activities=new ArrayList<>();
    public static DuskyApp getInstance() {
        return mInstance;
    }
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
        initBaiduMap();
        initJpush();
        initTiny();
        initImagePicker();
        crashHandler.init(this);
        registerActivityLifecycleCallbacks(this);
        PgyCrashManager.register(this);
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        PgyCrashManager.unregister();
        super.onTerminate();
    }


    private void init() {

        //配置glide圆角
        optionsRoundedCorners  = new RequestOptions()
                .centerCrop()
                //.error(R.drawable.zhanweitu)
                .priority(Priority.HIGH)
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //头像圆形配置
        optionsRoundedCircle  = new RequestOptions()
                .centerCrop()
                //.error(R.drawable.zhanweitu)
                .priority(Priority.HIGH)
                .transform(new CircleCrop())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    }
    private void initJpush(){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    private void initTiny() {
        Tiny.getInstance().init(this);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(9);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initBaiduMap(){
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);
    }


    public void logout(){
        //JPushInterface.cleanTags(this,1042032943);
        PreferenceUtil.resetPrivate();
        ToastUtil.ShortToast("已退出，请重新登录");

    }
    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            appConfig.versionName = pi.versionName;
            appConfig.versioncode = pi.versionCode;
            versionName=appConfig.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }


    Activity contextActivity;
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        activities.add(activity);
        if(activity.getParent()!=null){
            contextActivity = activity.getParent();
        }else
            contextActivity = activity;

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public void Exit(){
        int size=activities.size();
        for(int i=0;i<size;i++){
            if(activities.get(i)!=null){
                activities.get(i).finish();
            }
        }
    }

}