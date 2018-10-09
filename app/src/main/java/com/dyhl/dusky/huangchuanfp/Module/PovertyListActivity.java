package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;

import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PovertyListActivity extends BaseActivity {

    protected ProgressDialog dialog;
    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private List<PovertyInformation> datas;
    private PovertyListAdapter adapter;
    private boolean mIsRefreshing = false;
    int currentPage=1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_povertylist;
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
        mIsRefreshing=true;
        dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        dialog.show();
        String id=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);
        RetrofitHelper.getPovertyListAPI()
                .getList(id,currentPage+"",20+"")
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                PovertyInformation povertyInformation= JSON.parseObject(o, PovertyInformation.class);
                                datas.add(povertyInformation);
                            }
                            adapter.notifyItemRangeChanged(((currentPage-1)*20),20);
                            currentPage += 1;

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });


    }


    public void initRecyclerView(){
        datas=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new PovertyListAdapter(datas,this);
        recyclerView.setAdapter(adapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData();

            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();
        adapter.setOnItemClickListener(new PovertyListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent it=new Intent(PovertyListActivity.this, PovertyDetailActivity.class);
                it.putExtra("PovertyInfo",datas.get(position));
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
        setAppTitle("贫困户");
        initRecyclerView();
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
