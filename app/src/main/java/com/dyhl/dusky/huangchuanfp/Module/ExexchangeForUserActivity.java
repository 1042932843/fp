package com.dyhl.dusky.huangchuanfp.Module;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.dyhl.dusky.huangchuanfp.Adapter.ExexchangeAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.ExDrawerFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.ExCommonData;
import com.dyhl.dusky.huangchuanfp.Module.entity.Exp;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ExexchangeForUserActivity extends BaseActivity{


    int currentPage=1;
    int pageSize=10;
    ArrayList<Exp> datas;

    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @OnClick(R.id.mine)
    public void goMine(){
        Intent it=new Intent(this,ExMineActivity.class);
        startActivity(it);
    }

    @BindView(R.id.txt_title)
    TextView apptitle;


    @BindView(R.id.img_right)
    ImageView img_r;
    @OnClick(R.id.img_right)
    public void write(){
        Intent it=new Intent(this,ExPushActivity.class);
        startActivity(it);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event){
        if("refresh".equals(event)){
            currentPage=1;
            datas.clear();
            adapter.notifyDataSetChanged();
            loadData();
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_exexchangeforuser;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
        initRecyclerView();
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

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    ExexchangeAdapter adapter;
    private boolean mIsRefreshing = false;
    public void initRecyclerView(){
        datas=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ExexchangeAdapter(datas,this);
        recyclerView.setAdapter(adapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData();

            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();
        adapter.setOnItemClickListener(new ExexchangeAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent it=new Intent(ExexchangeForUserActivity.this,ExDetailActivity.class);
                it.putExtra("expid",datas.get(position).getExpid());
                startActivity(it);
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





    @Override
    public void initToolBar() {

    }

    String totalkey="",singlekey="",codes="",titles="",value="",names="",start="",end="",publics="";
    @SuppressLint("CheckResult")
    @Override
    public void loadData() {
        mIsRefreshing=true;
        String userid= PreferenceUtil.getStringPRIVATE("id", "");
        if(!TextUtils.isEmpty(userid)){
            totalkey=userid;
        }
       RetrofitHelper.getExperienceServiceAPI()
                .getList(singlekey,totalkey,codes,titles,value,names,publics,start,end,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                Exp povertyInformation= JSON.parseObject(o, Exp.class);
                                datas.add(povertyInformation);
                            }
                            adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                            currentPage += 1;
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    //tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }


    private void initWidget() {
        setAppTitle("经验交流");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }


}





