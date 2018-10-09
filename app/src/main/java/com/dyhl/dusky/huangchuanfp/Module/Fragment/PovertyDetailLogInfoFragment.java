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
import com.dyhl.dusky.huangchuanfp.Adapter.ChoiceAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SignListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.LogDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.LoglistActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Module.entity.SignInList;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PovertyDetailLogInfoFragment extends BaseFragment {
    private String type;
    int currentPage=1;
    private boolean mIsRefreshing = false;
    List<SignInList> datas;
    private SignListAdapter adapter;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tip)
    TextView tip;

    protected ProgressDialog dialog;

    String idcard;
    public static PovertyDetailLogInfoFragment newInstance(String type,String id) {
        PovertyDetailLogInfoFragment fragment=  new PovertyDetailLogInfoFragment();
        fragment.type=type;
        fragment.idcard=id;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_poverty_loginfo;
    }


    @Override
    public void finishCreateView(Bundle state) {
        initRec();
        loadData();
    }

    public void initRec(){


        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData();

            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        recyclerView.setLayoutManager(layoutManager);
        setRecycleNoScroll();
        datas=new ArrayList<>();
        adapter=new SignListAdapter(datas,getActivity());
        adapter.setOnItemClickListener(new SignListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                //ToastUtil.showShort(LoglistActivity.this,position+"");
                Intent it=new Intent(getActivity(),LogDetailActivity.class);
                it.putExtra("num",datas.get(position).getNum());
                startActivity(it);
            }
            @Override
            public void onLongClick(int position) {
                //ToastUtil.ShortToast(position+"");
            }
        });
        recyclerView.setAdapter(adapter);
    }
    @SuppressLint("CheckResult")
    public void loadData(){
        mIsRefreshing=true;
        dialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        RetrofitHelper.getPovertyListAPI()
                .getLogData(idcard,currentPage+"",10+"")
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
                                SignInList signInList= JSON.parseObject(o, SignInList.class);
                                datas.add(signInList);
                            }
                            if(datas.size()<=0){
                                tip.setHint("没有查询到相关信息");
                                break;
                                //ToastUtil.ShortToast("该户无相关日志信息");
                            }
                            adapter.notifyDataSetChanged();
                            currentPage++;
                            tip.setHint("");
                            break;
                        case "-1":
                        case "-2":
                            tip.setHint("没有查询到相关信息");
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                    mIsRefreshing=false;
                    dialog.dismiss();
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing=false;
                    tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
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
