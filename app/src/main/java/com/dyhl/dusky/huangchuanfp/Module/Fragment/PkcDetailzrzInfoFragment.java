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
import com.dyhl.dusky.huangchuanfp.Adapter.GzdInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.ZrzInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.GzdInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PkcDetailzrzInfoFragment extends BaseFragment {
    PkcInfo pkcInfo;
    List<GzdInfo> datas;

    int currentPage=1;
    int pageSize=20;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tip)
    TextView tip;

    ZrzInfoAdapter adapter;
    protected ProgressDialog dialog;

    public static PkcDetailzrzInfoFragment newInstance(PkcInfo pkcInfo) {
        PkcDetailzrzInfoFragment fragment=  new PkcDetailzrzInfoFragment();
        fragment.pkcInfo=pkcInfo;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_poverty_familyinfo;
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
        recyclerView.setLayoutManager(layoutManager);
        datas=new ArrayList<>();
        adapter=new ZrzInfoAdapter(datas,getActivity());
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    public void loadData(){
        dialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        RetrofitHelper.getPkcListServiceAPI()
                .getGkzrz(pkcInfo.getCode(),currentPage,pageSize)
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
                                GzdInfo povertyInformation= JSON.parseObject(o, GzdInfo.class);
                                datas.add(povertyInformation);
                            }
                            if(datas.size()<=0){
                                tip.setHint("没有查询到相关信息");
                                return;
                            }
                            currentPage++;
                            adapter.notifyDataSetChanged();
                            tip.setHint("");
                            break;
                        case "-1":
                        case "-2":
                            tip.setHint("没有查询到相关信息");
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                    dialog.dismiss();
                }, throwable -> {
                    dialog.dismiss();
                    tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
