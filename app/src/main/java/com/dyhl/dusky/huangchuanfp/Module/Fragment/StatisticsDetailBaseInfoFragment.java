package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.StatisticsDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyCondition;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyDetail;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class StatisticsDetailBaseInfoFragment extends BaseFragment {
    List<BaseInfo> datas;
    String idcard;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tip)
    TextView tip;
    PovertyBaseInfoAdapter adapter;

    public static StatisticsDetailBaseInfoFragment newInstance() {
        StatisticsDetailBaseInfoFragment fragment=  new StatisticsDetailBaseInfoFragment();
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_poverty_baseinfo;
    }


    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        initRec();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event){
        if(!TextUtils.isEmpty(event)) {
            switch (event) {
                case "StatisticsDetailBaseInfoFragment":
                    loadData();
                    break;

            }
        }

    }

    public void initRec(){
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        datas=new ArrayList<>();
        adapter=new PovertyBaseInfoAdapter(datas,getActivity());
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    public void loadData(){
        datas.clear();
        RetrofitHelper.getStatisticsAPI()
                .getpovertyCondition(StatisticsDetailActivity.permissionstag)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            PovertyCondition povertyInformation= JSON.parseObject(apiMsg.getResult(),PovertyCondition.class);
                            if(povertyInformation==null){
                                tip.setHint("没有查询到相关信息");
                                return;
                            }
                            BaseInfo baseInfo=new BaseInfo();
                            if(povertyInformation.getTotalfamily()!=null){
                                baseInfo.setKey("总户数");
                                baseInfo.setValue(povertyInformation.getTotalfamily()+"(户)");
                                datas.add(baseInfo);
                            }

                            BaseInfo baseInfo2=new BaseInfo();
                            if(povertyInformation.getTotalpopulation()!=null){
                                baseInfo2.setKey("总人数");
                                baseInfo2.setValue(povertyInformation.getTotalpopulation()+"(人)");
                                datas.add(baseInfo2);
                            }

                            BaseInfo baseInfo3=new BaseInfo();
                            if(povertyInformation.getPovertyfamily()!=null){
                                baseInfo3.setKey("贫困户数");
                                baseInfo3.setValue(povertyInformation.getPovertyfamily()+"(户)");
                                datas.add(baseInfo3);
                            }
                            BaseInfo baseInfo31=new BaseInfo();
                            if(povertyInformation.getOutpovertyfamily()!=null){
                                baseInfo31.setKey("脱贫户数");
                                baseInfo31.setValue(povertyInformation.getOutpovertyfamily()+"(户)");
                                datas.add(baseInfo31);
                            }

                            BaseInfo baseInfo41=new BaseInfo();
                            if(povertyInformation.getPovertypopulation()!=null){
                                baseInfo41.setKey("贫困人口数");
                                baseInfo41.setValue(povertyInformation.getPovertypopulation()+"(人)");
                                datas.add(baseInfo41);
                            }
                            BaseInfo baseInfo4=new BaseInfo();
                            if(povertyInformation.getPopulation()!=null){
                                baseInfo4.setKey("脱贫人数");
                                baseInfo4.setValue(povertyInformation.getPopulation()+"(人)");
                                datas.add(baseInfo4);
                            }

                            BaseInfo baseInfo5=new BaseInfo();
                            if(povertyInformation.getPovertyvillage()!=null){
                                baseInfo5.setKey("贫困村数");
                                baseInfo5.setValue(povertyInformation.getPovertyvillage()+"(个)");
                                datas.add(baseInfo5);
                            }

                            BaseInfo baseInfo6=new BaseInfo();
                            if(povertyInformation.getNotpovertyvillage()!=null){
                                baseInfo6.setKey("非贫困村数");
                                baseInfo6.setValue(povertyInformation.getNotpovertyvillage()+"(个)");
                                datas.add(baseInfo6);
                            }

                            BaseInfo baseInfo7=new BaseInfo();
                            if(povertyInformation.getZcdy()!=null){
                                baseInfo7.setKey("驻村工作队员数");
                                baseInfo7.setValue(povertyInformation.getZcdy()+"(人)");
                                datas.add(baseInfo7);
                            }

                            BaseInfo baseInfo8=new BaseInfo();
                            if(povertyInformation.getResponsible()!=null){
                                baseInfo8.setKey("帮扶责任人数");
                                baseInfo8.setValue(povertyInformation.getResponsible()+"(人)");
                                datas.add(baseInfo8);
                            }

                            BaseInfo baseInfo9=new BaseInfo();
                            if(povertyInformation.getBfzfl()!=null){
                                baseInfo9.setKey("帮扶走访量");
                                baseInfo9.setValue(povertyInformation.getBfzfl()+"(次)");
                                datas.add(baseInfo9);
                            }

                            adapter.notifyDataSetChanged();
                            tip.setHint("");
                            break;
                        case "-1":
                        case "-2":
                            tip.setHint("没有查询到相关信息");
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                }, throwable -> {
                    tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
