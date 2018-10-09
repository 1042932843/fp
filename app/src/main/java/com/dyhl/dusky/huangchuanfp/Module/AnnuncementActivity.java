package com.dyhl.dusky.huangchuanfp.Module;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.AnnuncementAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.AnnunDrawerFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Module.entity.AnnuncementCommonData;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnnuncementActivity extends BaseActivity {


    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;

    @BindView(R.id.total)
    TextView totalTv;
    String total;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private List<Annuncement> datas;
    private AnnuncementAdapter adapter;
    private boolean mIsRefreshing = false;
    int currentPage=1;
    SharedPreferences sharedPreferences;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.img_right)
    ImageView img_r;
    @OnClick(R.id.img_right)
    public void fabu(){
       String role= PreferenceUtil.getStringPRIVATE("role", UserState.NA);
        if (role.equals("2")) {
            Intent it=new Intent(AnnuncementActivity.this,PolicyinterpretationDetailActivity.class);
            it.putExtra("id","-10086");
            it.putExtra("type","notice");
            startActivityForResult(it,10086);
        } else {
            ToastUtil.ShortToast("只有管理员可以编辑和发布");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10086){
            mIsRefreshing = true;
            currentPage=1;
            datas.clear();
            adapter.notifyDataSetChanged();
            mEndlessRecyclerOnScrollListener.refresh();
            loadData();
        }
    }

    @BindView(R.id.img_right2)
    ImageView img_r2;
    @OnClick(R.id.img_right2)
    public void saixuan(){
        if(drawer.isDrawerOpen(Gravity.END)){
            drawer.closeDrawer(Gravity.END);
        }else{
            drawer.openDrawer(Gravity.END);
        }
    }

    String code,v_title="",v_source="",v_publics="",v_start="",v_end="";
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AnnuncementCommonData event){
        if(event!=null){
            currentPage=1;
            datas.clear();
            adapter.notifyDataSetChanged();
            v_title=event.getV_title();
            v_source=event.getV_source();
            v_publics=event.getV_publics();
            v_start=event.getV_start();
            v_end=event.getV_end();
            code=event.getV_code();
            if(TextUtils.isEmpty(code)&&TextUtils.isEmpty(v_title)&&TextUtils.isEmpty(v_source)&&TextUtils.isEmpty(v_publics)&&TextUtils.isEmpty(v_start)&&TextUtils.isEmpty(v_end)){
                event.setLight(false);
            }else{
                event.setLight(true);
            }
            img_r2.setImageResource(event.isLight()?R.drawable.main2_saixuan_light:R.drawable.main2_saixuan);
            loadData();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_annuncement;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
        String role= PreferenceUtil.getStringPRIVATE("role", UserState.NA);
        if(role.equals("2")){
            img_r.setVisibility(View.VISIBLE);
        }else{
            img_r.setVisibility(View.GONE);
        }
        AnnunDrawerFragment commonListTypeDrawerFragment=AnnunDrawerFragment.newInstance(drawer,"notice");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, commonListTypeDrawerFragment);
        transaction.commit();

        sharedPreferences = getSharedPreferences("read_zc", MODE_PRIVATE);
        img_r2.setVisibility(View.VISIBLE);
        img_r2.setImageResource(R.drawable.main2_saixuan);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            currentPage=1;
            datas.clear();
            adapter.notifyDataSetChanged();
            mEndlessRecyclerOnScrollListener.refresh();
            loadData();
        });
    }

    @Override
    public void initToolBar() {

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
    public void loadData() {
            String url = ApiConstants.Base_URL + "selectNoticeList?";
            OkHttpClient okHttpClient = new OkHttpClient();
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
            multipartBodyBuilder.setType(MultipartBody.FORM);
            multipartBodyBuilder.addFormDataPart("type","notice");
            multipartBodyBuilder.addFormDataPart("source",v_source);
        if(code==null||code.equals("")){
            code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        }
            multipartBodyBuilder.addFormDataPart("code",code);
            multipartBodyBuilder.addFormDataPart("title",v_title);
            multipartBodyBuilder.addFormDataPart("publics",v_publics);
            multipartBodyBuilder.addFormDataPart("start",v_start);
            multipartBodyBuilder.addFormDataPart("end",v_end);
            multipartBodyBuilder.addFormDataPart("currentPage",currentPage+"");
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIsRefreshing=false;
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Log.d("reg", "result:" + str);
                    try {
                        JSONObject obj = new JSONObject(str);
                        JSONObject data= obj.getJSONObject("result");
                        try{
                            total="共"+data.getString("totalCount")+"条公告";
                            totalTv.setText(total);
                        }catch (Exception e){

                        }
                        JSONArray jsonArray = data.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String o = jsonArray.getString(i);
//                        RedFlag redFlag = JSON.parseObject(o, RedFlag.class);
                           Annuncement annuncement= JSON.parseObject(o, Annuncement.class);
                            String user=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);
                            String ids = sharedPreferences.getString(user+"ids", "");
                            if (ids.contains(annuncement.getId())){
                                annuncement.setRead(true);
                            }else{
                                annuncement.setRead(false);
                            }
                            datas.add(annuncement);
                        }
                        currentPage++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRangeChanged((currentPage-1)*20,20);
                            mIsRefreshing=false;
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    call.cancel();
                }
            });

    }

    public void initRecyclerView(){
        datas=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new AnnuncementAdapter(datas,this);
        recyclerView.setAdapter(adapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData();

            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();
        adapter.setOnItemClickListener(new AnnuncementAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String user=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);
                String ids = sharedPreferences.getString(user+"ids", "");
                if (ids.contains(datas.get(position).getId())){

                }else {
                    editor.putString(user+"ids",ids+","+datas.get(position).getId());
                    editor.apply();
                }
                datas.get(position).setRead(true);
                adapter.notifyItemChanged(position);
                Intent it=new Intent(AnnuncementActivity.this,PolicyinterpretationDetailActivity.class);
                it.putExtra("id",datas.get(position).getId());
                it.putExtra("type","notice");
                startActivity(it);
            }
            @Override
            public void onLongClick(int position) {
                //ToastUtil.ShortToast(position+"");
            }
        });
    }

    private void setRecycleNoScroll() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefreshing;
            }
        });
    }


    private void initWidget() {
        setAppTitle("通知公告");
        initRecyclerView();
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
