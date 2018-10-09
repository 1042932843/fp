package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyDetail;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LiableDetailBaseInfoFragment extends BaseFragment {
    private String type;
    List<BaseInfo> datas;
    Liable liable;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tip)
    TextView tip;
    PovertyBaseInfoAdapter adapter;

    public static LiableDetailBaseInfoFragment newInstance(String type, Liable liable) {
        LiableDetailBaseInfoFragment fragment=  new LiableDetailBaseInfoFragment();
        fragment.type=type;
        fragment.liable=liable;
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
                            baseInfo.setKey("联系方式");
                            baseInfo.setValue(liable.getResponsiblephone());
                            datas.add(baseInfo);
                            BaseInfo baseInfo2=new BaseInfo();
                            baseInfo2.setKey("所在单位");
                            baseInfo2.setValue(liable.getDepartment());
                            datas.add(baseInfo2);

        BaseInfo baseInfo0=new BaseInfo();
        baseInfo0.setKey("职务");
        baseInfo0.setValue(liable.getPosition());
        datas.add(baseInfo0);

                            BaseInfo baseInfo3=new BaseInfo();
                            baseInfo3.setKey("帮扶数量");
                            baseInfo3.setValue(liable.getBfpkh());
                            datas.add(baseInfo3);
                            BaseInfo baseInfo4=new BaseInfo();
                            baseInfo4.setKey("走访量");
                            baseInfo4.setValue(liable.getBfzfl());
                            datas.add(baseInfo4);
                            adapter.notifyDataSetChanged();
                            tip.setHint("");

    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
