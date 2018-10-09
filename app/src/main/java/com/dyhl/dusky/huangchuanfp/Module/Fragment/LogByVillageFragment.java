package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
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
import com.dyhl.dusky.huangchuanfp.Adapter.LogScreenAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.logCommonData;
import com.dyhl.dusky.huangchuanfp.Module.entity.logScreenData;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LogByVillageFragment extends BaseFragment {

    String type;
    int currentPage=1;

    ArrayList<logScreenData> datas;
    LogScreenAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tip)
    TextView tip;

    logCommonData request;

    public static LogByVillageFragment newInstance(String type) {
        LogByVillageFragment fragment=  new LogByVillageFragment();
        fragment.type=type;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_log_bymonth;
    }


    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        request=new logCommonData();
        request.setType(type);
        initRec();
        loadData(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void initRec(){
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        datas=new ArrayList<>();
        adapter=new LogScreenAdapter(datas,type,getActivity());
        recyclerView.setAdapter(adapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(logCommonData request){
        if(request!=null) {
            this.request=request;
            switch (request.getType()) {
                case "4"://按村
                    datas.clear();
                    loadData(request);
                    break;

            }
        }

    }

    @SuppressLint("CheckResult")
    public void loadData(logCommonData request){
        if(TextUtils.isEmpty(request.getCode())){
            String code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
            request.setCode(code);
        }
        /*RetrofitHelper.getLogServiceAPI()
                .logStatistics(request.getType(),request.getCode(),request.getTown(),request.getVillage(),request.getName(),request.getStart(),request.getEnd(),currentPage+"",20+"")
                //.compose(this.bindToLifecycle())
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
                                logScreenData logScreenData= JSON.parseObject(o, logScreenData.class);
                                datas.add(logScreenData);
                            }
                            if(datas.size()<=0){
                                tip.setHint("没有查询到相关信息");
                                break;
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

                }, throwable -> {
                    tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });*/
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
