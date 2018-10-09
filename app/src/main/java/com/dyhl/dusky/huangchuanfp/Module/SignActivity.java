package com.dyhl.dusky.huangchuanfp.Module;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;

import com.dyhl.dusky.huangchuanfp.Base.DuskyApp;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.ImagePickerAdapter;
import com.dyhl.dusky.huangchuanfp.Design.imagePicker.SelectDialog;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.ChoiceFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener,KeyEditText.KeyPreImeListener{

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    public static final int RESULT_CODE_FP = 1042;
    public PovertyInformation Choice;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 6;               //允许选择图片最大数
    Tiny.FileCompressOptions options;
    private LatLng ll;
    private String address="";
    private String time="";
    List<File> files;
    protected ProgressDialog dialog;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.address)
    TextView addressV;

    @BindView(R.id.edit_text)
    KeyEditText editText;
    @BindView(R.id.head)
    ImageView head;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.fp_name)
    TextView fpname;
    @OnClick(R.id.fp_name_layout)
    public void pickName(){
        //获取到FragmentManager，在V4包中通过getSupportFragmentManager，
        //在系统中原生的Fragment是通过getFragmentManager获得的。
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ChoiceFragment choiceFragment= ChoiceFragment.newInstance("sign");
        transaction.add(R.id.container, choiceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @BindView(R.id.time)
    TextView timeTv;

    @BindView(R.id.txt_right)
    TextView txt_right;
    @OnClick(R.id.txt_right)
    public void comple(){
       complete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
        initBaiduMap();
        files=new ArrayList<>();
        options = new Tiny.FileCompressOptions();
    }

    @Override
    public void initToolBar() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PovertyInformation event){
        if(event!=null){
            this.Choice=event;
            fpname.setText(Choice.getName());
        }
    }


    private void initBaiduMap() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        initoption();
        mLocClient.start();
    }
    void initoption() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span = 3000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setIsNeedLocationDescribe(true);
        mLocClient.setLocOption(option);
    }

    String rec="";

    @Override
    public void onKeyPreImeUp(int keyCode, KeyEvent event) {
        editText.clearFocus();

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {


        @Override
        public void onReceiveLocation(final BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            time=location.getTime();

            ll = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d("reg", "经纬度：" + ll);
            Log.d("reg", "location.getAddrStr()：" + address);
            address=location.getAddrStr();
            if(rec.equals(address)){

            }else{
                rec=address;
                //我在这里利用经度、纬度信息构建坐标点
                LatLng point = new LatLng(location.getLatitude(),
                        location.getLongitude());
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        //更新UI
                        if(address!=null&&!"".equals(address)){
                            addressV.setText(" "+address);
                        }else{
                            address="没有获取到位置信息";
                            addressV.setText(" "+address);
                        }
                        timeTv.setText(" "+time);
                    }

                });
            }

            Log.d("reg", "经纬度：" + ll);
            Log.d("reg", "location.getAddrStr()：" + address);


        }





    }


    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        Log.d("reg", "onPause()");
        super.onPause();

    }
    private void initWidget() {
        setAppTitle("写日志");
        selImageList = new ArrayList<>();
        editText.setKeyPreImeListener(this);
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        txt_right.setText("提交");
        txt_right.setTextColor(getResources().getColor(R.color.colorPrimary));
        fpname.setText("请选择扶贫对象");
        dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
    }
    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        TextView apptitle= (TextView) findViewById(R.id.txt_title);
        apptitle.setText(title);
    }

    public void complete(){
        if(address==null||address.equals("")||address.equals("没有获取到位置信息")){
            ToastUtil.showShort(this,"定位失败无法提交");
            return;
        }
        if(Choice==null){
            ToastUtil.showShort(this,"请选择扶贫对象");
            return;
        }
        String url= ApiConstants.Base_URL + "saveWorkPicture";
        String pic_key="pictures";
        Map<String ,String > params=new HashMap<>();
        String id=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);
        params.put("key",id);
        params.put("name",Choice.getName());//贫困户的名字
        params.put("idcard",Choice.getIdcard());//贫困户的身份
        String rz=editText.getText().toString()+"";
        params.put("value ", rz);//用户日志
        params.put("adress",address+"");//定位位置
        params.put("time",time);//提交时间
        params.put("code",Choice.getCode());
        uploadPic(url,params,pic_key,files);

    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }
    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(SignActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(SignActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }
    ArrayList<ImageItem> images = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    Log.d("reg","images:"+images.get(0).path);
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                    files.clear();

                    for(int i=0;i<selImageList.size();i++){

                        Tiny.getInstance().source(selImageList.get(i).path).asFile().withOptions(options).compress(new FileCallback() {
                            @Override
                            public void callback(boolean isSuccess, String outfile) {
                                File file = new File(outfile);
                                files.add(file);
                            }
                        });

                    }

                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                    files.clear();
                    for(int i=0;i<selImageList.size();i++){
                        Tiny.getInstance().source(selImageList.get(i).path).asFile().withOptions(options).compress(new FileCallback() {
                            @Override
                            public void callback(boolean isSuccess, String outfile) {
                                File file = new File(outfile);
                                files.add(file);
                            }
                        });
                    }
                }
            }
        }else if(resultCode==RESULT_CODE_FP){

        }
    }


    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    public void uploadPic(String reqUrl, Map<String, String> params, String pic_key, List<File> files){
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        //遍历map中所有参数到builder
        if (params != null){
            for (String key : params.keySet()) {
                if(null!=params.get(key)){
                    multipartBodyBuilder.addFormDataPart(key, params.get(key));
                }else{
                    multipartBodyBuilder.addFormDataPart(key, "");
                }

            }
        }

        //遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
        if (files != null){
            for (File file : files) {
                multipartBodyBuilder.addFormDataPart(pic_key, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
            }
        }
        dialog.show();
        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(reqUrl);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                    call.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });
        }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d("reg", "result:" + str);
                final ApiMsg apiMsg = JSON.parseObject(str, ApiMsg.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showLong(SignActivity.this,apiMsg.getMessage());
                        dialog.dismiss();
                        if(apiMsg.isSuccess()){
                            finish();
                        }

                    }
                });
                call.cancel();
            }
        });
    }



}
