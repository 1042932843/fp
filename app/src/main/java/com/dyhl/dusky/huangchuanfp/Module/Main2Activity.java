package com.dyhl.dusky.huangchuanfp.Module;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.DuskyApp;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.NotificationUtils;
import com.dyhl.dusky.huangchuanfp.Utils.PgyUpdateManagerListener;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 这是领导页
 */
public class Main2Activity extends BaseActivity {
    public static final String MESSAGE_RECEIVED_ACTION = "MESSAGE_RECEIVED_ACTION";

    AlphaAnimation alphaAnimation1;
    @BindView(R.id.title)
    TextView textView;

    @BindView(R.id.user_head)
    ImageView user_head;
    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.role)
    TextView roletv;

    @BindView(R.id.annuncement)
    TextView annuncement;

    @BindView(R.id.laba)
    ImageView laba;

    @BindView(R.id.ver)
    TextView ver;

    @OnClick(R.id.set)
    public void goSet(){
        Intent it=new Intent(Main2Activity.this,SetActivity.class);
        startActivity(it);
    }

    SharedPreferences sharedPreferences;

    @OnClick(R.id.annuncement_layout)
    public void goAnnuncementDetail(){
        if("notice".equals(type)){
            Intent i = new Intent(this, AnnuncementActivity.class);
            startActivity(i);
        }
       else if("news".equals(type)){
            Intent i = new Intent(this, PolicyinterpretationActivity.class);
            startActivity(i);
        }else {
            startActivity(new Intent(this, AnnuncementActivity.class));
        }
        point.setVisibility(View.GONE);
        pointu.setVisibility(View.GONE);
    }

    @OnClick(R.id.xtgg)
    public void goAnnuncement(){
        startActivity(new Intent(this, AnnuncementActivity.class));
        point.setVisibility(View.GONE);
        pointu.setVisibility(View.GONE);
    }
    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.point)
    ImageView point;

    @BindView(R.id.point_u)
    ImageView pointu;

    @OnClick(R.id.dl)
    public void open(){
        if(drawer.isDrawerOpen(Gravity.START)){
            drawer.closeDrawer(Gravity.START);
        }else{
            drawer.openDrawer(Gravity.START);
        }

    }


    public void showToast(){
        ToastUtil.showShort(this,"功能开发中,请留意后续更新");
    }

    @OnClick(R.id.main2_sjfx)
    public void goStatistics(){
        Intent it=new Intent(Main2Activity.this,StatisticsDetailActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.main2_rztj)
    public void goRZTJ(){
        Intent it=new Intent(Main2Activity.this,LogScreenListActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.main2_zcjd)
    public void goZCJD(){
        Intent it=new Intent(Main2Activity.this,PolicyinterpretationActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.main2_jyjl)
    public void goJYJL(){
        Intent it=new Intent(Main2Activity.this,ExexchangeActivity.class);
        startActivity(it);
        //showToast();
    }

    @OnClick({R.id.main2_bfcs})
    public void goWhere(){
        showToast();
    }

    @OnClick({R.id.main2_bfdw,R.id.main2_pkc,R.id.main2_pkh,R.id.main2_bfzrr,R.id.main2_bfrz,R.id.main2_sbwt,R.id.fppush,R.id.main2_dysj,R.id.main2_gzd,R.id.main2_zrz})
    public void goCommonListActivity(View view){
        if (view.getId()==R.id.fppush){
            Intent it=new Intent(Main2Activity.this,PovertyProblemListActivity.class);
            startActivity(it);
        }else{
            if(view.getId()==R.id.main2_pkc){
                String code=PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
                if(code.length()<=6){
                    Intent it=new Intent(Main2Activity.this,PkcActivity.class);
                    startActivity(it);
                }else{
                    Intent it=new Intent(Main2Activity.this,CommonListActivity.class);
                    it.putExtra("position",view.getId());
                    startActivity(it);
                }
            }else{
                Intent it=new Intent(Main2Activity.this,CommonListActivity.class);
                it.putExtra("position",view.getId());
                startActivity(it);
            }

        }

    }

    @OnClick(R.id.main2_wdbf)
    public void goMap(){
        Intent it=new Intent(Main2Activity.this,MapActivity.class);
        it.putExtra("from","Main2Activity");
        startActivity(it);
    }

    @BindView(R.id.back)
    TextView wdbf;
    @OnClick(R.id.back)
    public void goMap2(){
        Intent it=new Intent(Main2Activity.this,MapActivity.class);
        it.putExtra("from","Main2Activity");
        startActivity(it);
    }

    @OnClick(R.id.rz)
    public void goRZ(){
        Intent it=new Intent(Main2Activity.this, LoglistActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.tc)
    public void quit(){
        DuskyApp.getInstance().logout();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.wdjl)
    public void goWDJL(){
        Intent it=new Intent(this,ExMineActivity.class);
        startActivity(it);
    }

    /**
     * 检查登录,没登录去登录页面,登录了检查状态
     */
    public void CheckLogin(){
        String s= PreferenceUtil.getStringPRIVATE("id", UserState.NA);
        //LogUtil.d(s);
        if(s.isEmpty()||UserState.NA.equals(s)){
            ToastUtil.ShortToast("请登录！");
            Intent it=new Intent(this, LoginActivity.class);
            startActivity(it);
        }else{
            update();
            initUser();
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



    String type="";
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Annuncement event){
        if(event!=null){
            startAnim();
            String title=event.getTitle();
            String id=event.getTitle();
            annuncement.setText(title);
            type=event.getType();
            PreferenceUtil.putStringPRIVATE("newsID",id);
            PreferenceUtil.putStringPRIVATE("newsTitle",title);
            point.setVisibility(View.VISIBLE);
            if(type.equals("notice")){
                pointu.setVisibility(View.VISIBLE);
            }

        }
    }

    public void initAn(Annuncement an){

        if(!TextUtils.isEmpty(an.getTitle())){
            annuncement.setText(an.getTitle());
        }else{
            annuncement.setText("没有新的公告");
        }
    }

    public void initAnim(){
        alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(500);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);


    }

    public void startAnim(){
        laba.setAnimation(alphaAnimation1);
        alphaAnimation1.start();
    }

    public void stopAnim(){
        laba.clearAnimation();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("read_zc", MODE_PRIVATE);
        ver.setHint("版本号："+DuskyApp.getInstance().getAppVersionName(this));
        wdbf.setText("我的帮扶");
        wdbf.setVisibility(View.VISIBLE);
        CheckLogin();
        initAnim();//公告喇叭动画
        //loadNotice(); //初始化公告，每次从sp中读取
    }

    public void loadNotice(){
        String url = ApiConstants.Base_URL + "selectNoticeList?";
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        String code=PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        multipartBodyBuilder.addFormDataPart("code",code);
        multipartBodyBuilder.addFormDataPart("type","notice");
        multipartBodyBuilder.addFormDataPart("currentPage",1+"");
        multipartBodyBuilder.addFormDataPart("pageSize", 10+"");
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(url);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                call.cancel();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d("reg", "result:" + str);
                try {
                    JSONObject obj = new JSONObject(str);
                    JSONObject data= obj.getJSONObject("result");
                    JSONArray jsonArray = data.getJSONArray("data");
                    String user=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String o2 = jsonArray.getString(i);
                        Annuncement annuncement2= JSON.parseObject(o2, Annuncement.class);
                        String ids = sharedPreferences.getString(user+"ids", "");
                        if (!ids.contains(annuncement2.getId())){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    point.setVisibility(View.VISIBLE);
                                    pointu.setVisibility(View.VISIBLE);
                                    initAn(annuncement2);
                                }
                            });
                            break;
                        }else{

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                call.cancel();
            }
        });
    }

    private void initUser() {
        String name=  PreferenceUtil.getStringPRIVATE("name",UserState.NA);
        if(!UserState.NA.equals(name)){
            user_name.setText(name);
        }
        String picture=  PreferenceUtil.getStringPRIVATE("picture",UserState.NA);
        if(!picture.equals(UserState.NA)&&!picture.equals("")){
            Glide.with(this).load(ApiConstants.Base_URL+picture).apply(DuskyApp.optionsRoundedCircle).into(user_head);
        }else{
            Glide.with(this).load(R.drawable.def_head).apply(DuskyApp.optionsRoundedCircle).into(user_head);
        }
        String role= PreferenceUtil.getStringPRIVATE("role", UserState.NA);
        if(!role.equals(UserState.NA)){
            if(role.equals("2")){
                roletv.setText("");
            }else if(role.equals("1")){
                roletv.setText("帮扶责任人");
            }
        }else{

        }
        String phonet=PreferenceUtil.getStringPRIVATE("phone", UserState.NA);
        if(!phonet.equals(UserState.NA)){
            phone.setText(phonet);
        }

    }


    @Override
    public void initToolBar() {
        textView.setText(getText(R.string.app_name));
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        loadNotice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        stopAnim();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        PgyUpdateManager.unregister();
    }

    private long onBackPressedTimeMillis; // 按下返回键的时间戳
    @Override
    public void onBackPressed() { // 连续按下两次返回键才退出App
        long currentTimeMillis = System.currentTimeMillis();
        if (onBackPressedTimeMillis != 0 && currentTimeMillis - onBackPressedTimeMillis < 3000) {
            DuskyApp.getInstance().Exit();
            super.onBackPressed();
        } else {
            ToastUtil.LongToast("再按一次退出app");

        }
        onBackPressedTimeMillis = currentTimeMillis;
    }
}



