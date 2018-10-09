package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.ExexchangeAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.PovertyDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.Exp;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ExMineFragment extends BaseFragment {

    String type;

    @BindView(R.id.play_list)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tip)
    TextView tip;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    ArrayList<Exp> datas;
    private ExexchangeAdapter adapter;
    private boolean mIsRefreshing = false;
    int currentPage=1;
    int pageSize=20;

    String totalkey="",singlekey="",codes="",titles="",value="",names="",start="",end="",publics="";

    public static ExMineFragment newInstance(String type) {
        ExMineFragment fragment=  new ExMineFragment();
        fragment.type=type;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_exmine;
    }

    String userid;
    @Override
    public void finishCreateView(Bundle state) {
        userid = PreferenceUtil.getStringPRIVATE("id", "");
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            currentPage=1;
            datas.clear();
            adapter.notifyDataSetChanged();
            mEndlessRecyclerOnScrollListener.refresh();
            loadData();
        });
        initRecyclerView();
        loadData();
    }

    @SuppressLint("CheckResult")
    public void loadData() {
        switch (type){
            case "全部":
                singlekey=userid;
                break;
            case "公开":
                publics=type;
                singlekey=userid;
                break;
            case "屏蔽":
                publics=type;
                singlekey=userid;
                break;
        }
        mIsRefreshing=true;
        RetrofitHelper.getExperienceServiceAPI()
                .getList(singlekey,totalkey,codes,titles,value,names,publics,start,end,currentPage,pageSize)
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

    public void initRecyclerView(){
        datas=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ExexchangeAdapter(datas,getActivity());
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
