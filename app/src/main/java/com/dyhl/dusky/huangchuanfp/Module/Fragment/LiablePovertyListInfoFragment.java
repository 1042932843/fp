package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.PovertyDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.PovertyListActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
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


public class LiablePovertyListInfoFragment extends BaseFragment {
    private String type;

    Liable liable;
    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @BindView(R.id.tip)
    TextView tip;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private List<PovertyInformation> datas;
    private PovertyListAdapter adapter;
    private boolean mIsRefreshing = false;
    int currentPage=1;



    public static LiablePovertyListInfoFragment newInstance(String type, Liable liable) {
        LiablePovertyListInfoFragment fragment=  new LiablePovertyListInfoFragment();
        fragment.type=type;
        fragment.liable=liable;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_liable_povertylist;
    }


    @Override
    public void finishCreateView(Bundle state) {
        initRecyclerView();
        loadData();
    }

    @SuppressLint("CheckResult")
    public void loadData() {
        mIsRefreshing=true;
        RetrofitHelper.getPovertyListAPI()
                .getList(liable.getResponsibleid(),currentPage+"",20+"")
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
                            if(datas.size()>0){
                                tip.setText("");
                                adapter.notifyItemRangeChanged(((currentPage-1)*20),20);
                                currentPage += 1;
                            }else{
                                tip.setText("没有查询到相关信息");
                            }



                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    mIsRefreshing = false;
                }, throwable -> {
                    mIsRefreshing = false;
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
        adapter=new PovertyListAdapter(datas,getActivity());
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
                Intent it=new Intent(getActivity(), PovertyDetailActivity.class);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
