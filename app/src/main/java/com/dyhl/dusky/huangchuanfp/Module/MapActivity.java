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
import android.support.v7.widget.RecyclerView;
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
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Module.entity.User;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Utils.NotificationUtils;
import com.dyhl.dusky.huangchuanfp.R;
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

public class MapActivity extends BaseActivity {
    public static final String MESSAGE_RECEIVED_ACTION = "MESSAGE_RECEIVED_ACTION";
    private LocationClient mClient;
    private MyLocationListener myLocationListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private NotificationUtils mNotificationUtils;
    private Notification notification;

    private String address; //定位地址
    private LatLng ll;
    private boolean isFirstLoc = true;
    private boolean isEnableLocInForeground = false;
    SharedPreferences sharedPreferences;
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

    @BindView(R.id.bottomSheetLayout)
    View  bottomSheetLayout;
    private BottomSheetBehavior bottomSheetBehavior;


    @OnTouch(R.id.bottom_bar)
    public boolean dialog(){
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        return true;
    }

    @OnClick(R.id.annuncement_layout)
    public void goAnnuncementDetail(){
        startActivity(new Intent(this, AnnuncementActivity.class));
        point.setVisibility(View.GONE);
        pointu.setVisibility(View.GONE);
    }

    @OnClick(R.id.xtgg)
    public void goAnnuncement(){
        startActivity(new Intent(this, AnnuncementActivity.class));
        point.setVisibility(View.GONE);
        pointu.setVisibility(View.GONE);
    }
    @OnClick(R.id.fppush)
    public void gopushList(){
        Intent it=new Intent(MapActivity.this,PovertyProblemListActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.set)
    public void goSet(){
        Intent it=new Intent(MapActivity.this,SetActivity.class);
        startActivity(it);
    }

    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.point)
    ImageView point;

    @BindView(R.id.point_u)
    ImageView pointu;

    @BindView(R.id.ver)
    TextView ver;

    @OnClick(R.id.dl)
    public void open(){
        if(drawer.isDrawerOpen(Gravity.START)){
            drawer.closeDrawer(Gravity.START);
        }else{
            drawer.openDrawer(Gravity.START);
        }

    }


    @BindView(R.id.back)
    TextView back;
    @OnClick(R.id.back)
    public void back(){
     finish();
    }

    @OnClick(R.id.wdrz)
    public void goWDRZ(){
        Intent it=new Intent(MapActivity.this, LoglistActivity.class);
        startActivity(it);
    }
    @OnClick(R.id.wtsb)
    public void goWTSB(){
        Intent it=new Intent(MapActivity.this,FpPushActivity.class);
        startActivity(it);
    }
    @OnClick(R.id.sbjl)
    public void goSBJL(){
        Intent it=new Intent(MapActivity.this,PovertyProblemListActivity.class);
        startActivity(it);
    }
    @OnClick(R.id.fpzc)
    public void goBFZC(){
        Intent it=new Intent(MapActivity.this,PolicyinterpretationActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.jyjl)
    public void goJYJL(){
        Intent it=new Intent(MapActivity.this,ExexchangeForUserActivity.class);
        startActivity(it);
    }

    @OnClick(R.id.gpkh)
    public void goPKH (){
        startActivity(new Intent(this, PovertyListActivity.class));
    }

    @OnClick(R.id.wlog)
    public void goSign() {
        startActivity(new Intent(this, SignActivity.class));
    }

    @OnClick(R.id.rz)
    public void goRZ(){
        Intent it=new Intent(MapActivity.this, LoglistActivity.class);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Annuncement event){
        if(event!=null){
            startAnim();
            String title=event.getTitle();
            String id=event.getTitle();
            annuncement.setText(title);
            PreferenceUtil.putStringPRIVATE("newsID",id);
            PreferenceUtil.putStringPRIVATE("newsTitle",title);
            point.setVisibility(View.VISIBLE);
            pointu.setVisibility(View.VISIBLE);
        }
    }

    public void initAn(Annuncement an){
        if(!TextUtils.isEmpty(an.getTitle())){
            annuncement.setText(an.getTitle());
        }else{
            annuncement.setText("没有新的公告");
        }
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
        multipartBodyBuilder.addFormDataPart("pageSize", 25+"");
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


    @OnClick(R.id.location)
    public void setLOC(){
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(ll)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.animateMapStatus(mMapStatusUpdate);
        ToastUtil.showShort(this,"已经定位到您当前的位置");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("read_zc", MODE_PRIVATE);
        String from=getIntent().getStringExtra("from");
        if("Main2Activity".equals(from)){
            back.setVisibility(View.VISIBLE);
        }
        ver.setHint("版本号："+DuskyApp.getInstance().getAppVersionName(this));
        CheckLogin();
        initMap();//百度地图
        initBottomSheetBehavior();//底部菜单
        initAnim();//公告喇叭动画


    }

    public void initBottomSheetBehavior(){
        bottomSheetBehavior=BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //bottomSheetHeading.setText(getString(R.string.text_collapse_me));
                } else {
                    //bottomSheetHeading.setText(getString(R.string.text_expand_me));
                }
                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }


            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

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

    private void initMap(){

        /*//设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("适配android 8限制后台定位功能", "正在后台定位");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(MapActivity.this);
            Intent nfIntent = new Intent(MapActivity.this, MapActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(MapActivity.this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("适配android 8限制后台定位功能") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
        }
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
*/
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //地图上比例尺
        mMapView.showScaleControl(false);
        // 隐藏缩放控件
        mMapView.showZoomControls(false);

        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        //        关闭百度地图上的操作 ：
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);   //关闭一切手势操作
        settings.setOverlookingGesturesEnabled(false);//屏蔽双指下拉时变成3D地图
        settings.setZoomGesturesEnabled(true);//获取是否允许缩放手势返回:是否允许缩放手势
        settings.setScrollGesturesEnabled(true);//拖拽
        // 定位初始化
        mClient = new LocationClient(this);
        LocationClientOption mOption = new LocationClientOption();
        mOption.setScanSpan(5000);
        mOption.setCoorType("bd09ll");
        mOption.setIsNeedAddress(true);
        mOption.setOpenGps(true);
        mClient.setLocOption(mOption);
        mClient.registerLocationListener(myLocationListener);
        mClient.start();
    }


    @Override
    public void initToolBar() {
        textView.setText(getText(R.string.app_name));
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        loadNotice();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        stopAnim();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(mClient!=null){
            mClient.disableLocInForeground(true);
            mClient.unRegisterLocationListener(myLocationListener);
            mClient.stop();
        }
        if(mMapView!=null){
            mMapView.onDestroy();
            mMapView = null;
        }
        EventBus.getDefault().unregister(this);
        PgyUpdateManager.unregister();
    }


    class  MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mMapView == null) {
                return;
            }
            ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

            MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            //地图SDK处理
            //发消息更新地址
            Message msg = new Message();
            msg.what = UPDATE_ADDDRESS;
            address = bdLocation.getAddrStr();
            handler.sendMessage(msg);

        }
    }
    public static final int UPDATE_ADDDRESS = 0;
    private MyHandler handler = new MyHandler(this);
    class MyHandler extends Handler {
        // 弱引用 ，防止内存泄露
        private WeakReference<MapActivity> weakReference;
        public MyHandler(MapActivity activity){
            weakReference = new WeakReference<MapActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 通过  软引用  看能否得到activity示例
            MapActivity activity = weakReference.get();
            // 防止内存泄露
            if (activity != null) {
                // 如果当前Activity，进行UI的更新
                switch (msg.what) {
                    case UPDATE_ADDDRESS:
                        Log.d("reg", "rz_address:" + address);
                        if(isFirstLoc){
                            isFirstLoc=false;
                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(ll)
                                    .zoom(18)
                                    .build();
                            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                            //改变地图状态
                            mBaiduMap.animateMapStatus(mMapStatusUpdate);

                        }
                }
            }else {
// 没有实例不进行操作
            }
        }
    }

    private long onBackPressedTimeMillis; // 按下返回键的时间戳
    @Override
    public void onBackPressed() { // 连续按下两次返回键才退出App
        String from=getIntent().getStringExtra("from");
        if("Main2Activity".equals(from)){
            super.onBackPressed();
            return;
        }
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
