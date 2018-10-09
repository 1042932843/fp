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
import com.dyhl.dusky.huangchuanfp.Adapter.ChoiceAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyFamilyInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyDetail;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
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

public class PovertyDetailFamilyInfoFragment extends BaseFragment {
    private String type;
    private  String familyid;
    List<PovertyInformation> datas;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tip)
    TextView tip;

    PovertyFamilyInfoAdapter adapter;
    protected ProgressDialog dialog;

    public static PovertyDetailFamilyInfoFragment newInstance(String type,String id) {
        PovertyDetailFamilyInfoFragment fragment=  new PovertyDetailFamilyInfoFragment();
        fragment.type=type;
        fragment.familyid=id;
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
        adapter=new PovertyFamilyInfoAdapter(datas,getActivity());
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    public void loadData(){
        dialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        RetrofitHelper.getPovertyListAPI()
                .getfamilyData(familyid)
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
                                //[{"idcard":"413024194012146313","phone":"","nation":"汉族","tpnd":"","position":"","id":"855510ce649d11e89370e0d55e42430b","reson":"","dangerousbuilding":"","education":"小学","peopleid":"310040497845","familyid":"3113335813","department":"","watersafety":"","overcomeattribute":"","worktime":"0","income":"","population":"","dysdipsia":"","povertyattribute":"","student":"非在校生","county":"","medical":"是","village":"","work":"","naturalvillage":"","wdtpnd":"","health":"长期慢性病","town":"","responsible":"","relationship":"户主","skill":"丧失劳动力","name":"王明甫","responsiblephone":""},{"idcard":"413024194112176325","phone":"","nation":"汉族","tpnd":"","position":"","id":"8555111f649d11e89370e0d55e42430b","reson":"","dangerousbuilding":"","education":"文盲或半文盲","peopleid":"310040497846","familyid":"3113335813","department":"","watersafety":"","overcomeattribute":"","worktime":"0","income":"","population":"","dysdipsia":"","povertyattribute":"","student":"非在校生","county":"","medical":"是","village":"","work":"","naturalvillage":"","wdtpnd":"","health":"长期慢性病","town":"","responsible":"","relationship":"配偶","skill":"丧失劳动力","name":"张桂芳","responsiblephone":""},{"idcard":"413024197310166316","phone":"","nation":"汉族","tpnd":"","position":"","id":"85551170649d11e89370e0d55e42430b","reson":"","dangerousbuilding":"","education":"初中","peopleid":"310040497847","familyid":"3113335813","department":"","watersafety":"","overcomeattribute":"","worktime":"10","income":"","population":"","dysdipsia":"","povertyattribute":"","student":"非在校生","county":"","medical":"是","village":"","work":"县外省内务工","naturalvillage":"","wdtpnd":"","health":"健康","town":"","responsible":"","relationship":"之子","skill":"普通劳动力","name":"王友库","responsiblephone":""},{"idcard":"413024197701156342","phone":"","nation":"汉族","tpnd":"","position":"","id":"855511c1649d11e89370e0d55e42430b","reson":"","dangerousbuilding":"","education":"初中","peopleid":"310040497848","familyid":"3113335813","department":"","watersafety":"","overcomeattribute":"","worktime":"0","income":"","population":"","dysdipsia":"","povertyattribute":"","student":"非在校生","county":"","medical":"是","village":"","work":"","naturalvillage":"","wdtpnd":"","health":"健康","town":"","responsible":"","relationship":"之儿媳","skill":"普通劳动力","name":"黄正萍","responsiblephone":""},{"idcard":"41152620051107631X","phone":"","nation":"汉族","tpnd":"","position":"","id":"85551212649d11e89370e0d55e42430b","reson":"","dangerousbuilding":"","education":"","peopleid":"310040497850","familyid":"3113335813","department":"","watersafety":"","overcomeattribute":"","worktime":"0","income":"","population":"","dysdipsia":"","povertyattribute":"","student":"七年级","county":"","medical":"是","village":"","work":"","naturalvillage":"","wdtpnd":"","health":"健康","town":"","responsible":"","relationship":"之孙子","skill":"无劳动力","name":"王俊","responsiblephone":""},{"idcard":"41152620020128632X","phone":"","nation":"汉族","tpnd":"","position":"","id":"8555125e649d11e89370e0d55e42430b","reson":"","dangerousbuilding":"","education":"","peopleid":"310040497849","familyid":"3113335813","department":"","watersafety":"","overcomeattribute":"","worktime":"0","income":"","population":"","dysdipsia":"","povertyattribute":"","student":"高中一年级","county":"","medical":"是","village":"","work":"","naturalvillage":"","wdtpnd":"","health":"健康","town":"","responsible":"","relationship":"之孙女","skill":"无劳动力","name":"王冰","responsiblephone":""}]
                                PovertyInformation povertyInformation= JSON.parseObject(o, PovertyInformation.class);
                                datas.add(povertyInformation);
                            }
                            if(datas.size()<=0){
                                tip.setHint("没有查询到相关信息");
                                return;
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
