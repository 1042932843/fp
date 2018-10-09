package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class PkcDetailBaseInfoFragment extends BaseFragment {
    List<BaseInfo> datas;
    PkcInfo pkcInfo;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tip)
    TextView tip;
    PovertyBaseInfoAdapter adapter;

    public static PkcDetailBaseInfoFragment newInstance(PkcInfo pkcInfo) {
        PkcDetailBaseInfoFragment fragment=  new PkcDetailBaseInfoFragment();
        fragment.pkcInfo=pkcInfo;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_poverty_baseinfo;
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
    @Override
    public void finishCreateView(Bundle state) {
        initRec();
        loadData();
    }

    @SuppressLint("CheckResult")
    public void loadData(){

                            BaseInfo baseInfo=new BaseInfo();
                            baseInfo.setKey("负责人");
                            baseInfo.setValue(pkcInfo.getIncharge());
                            datas.add(baseInfo);
                            BaseInfo baseInfo2=new BaseInfo();
                            baseInfo2.setKey("办公电话");
                            baseInfo2.setValue(pkcInfo.getPhone());
                            datas.add(baseInfo2);
        /*BaseInfo baseInfo0=new BaseInfo();
        baseInfo0.setKey("贫困状态");
        baseInfo0.setValue(pkcInfo.getType());
        datas.add(baseInfo0);*/
                            BaseInfo baseInfo3=new BaseInfo();
                            baseInfo3.setKey("是否脱贫");
                            baseInfo3.setValue(pkcInfo.getOutpoverty());
                            datas.add(baseInfo3);
                            BaseInfo baseInfo4=new BaseInfo();
                            baseInfo4.setKey("总户数");
                            baseInfo4.setValue(pkcInfo.getTotalfamily());
                            datas.add(baseInfo4);

                            BaseInfo baseInfo5=new BaseInfo();
                            baseInfo5.setKey("总人口数");
                            baseInfo5.setValue(pkcInfo.getTotalpopulation());
                            datas.add(baseInfo5);

        BaseInfo baseInfo6=new BaseInfo();
        baseInfo6.setKey("自然村数");
        baseInfo6.setValue(pkcInfo.getNaturalvillage());
        datas.add(baseInfo6);

        BaseInfo baseInfo7=new BaseInfo();
        baseInfo7.setKey("贫困户数");
        baseInfo7.setValue(pkcInfo.getPovertyfamily());
        datas.add(baseInfo7);

        BaseInfo baseInfo8=new BaseInfo();
        baseInfo8.setKey("贫困人口数");
        baseInfo8.setValue(pkcInfo.getPovertypopulation());
        datas.add(baseInfo8);
        tip.setHint("");

    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
