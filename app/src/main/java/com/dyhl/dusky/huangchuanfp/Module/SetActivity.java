package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.AnnuncementAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;

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

public class SetActivity extends BaseActivity {


    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.resetPw)
    public void goReset(){
        Intent it =new Intent(this,PwResetActivity.class);
        startActivity(it);
    }

    @BindView(R.id.txt_title)
    TextView apptitle;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private List<Annuncement> datas;
    private AnnuncementAdapter adapter;
    private boolean mIsRefreshing = false;
    int currentPage=1;


    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
    }

    @Override
    public void initToolBar() {

    }



    @Override
    public void loadData() {

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

            }
            @Override
            public void onLongClick(int position) {
                //ToastUtil.ShortToast(position+"");
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setRecycleNoScroll() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefreshing;
            }
        });
    }


    private void initWidget() {
        setAppTitle("设置");
        initRecyclerView();
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
