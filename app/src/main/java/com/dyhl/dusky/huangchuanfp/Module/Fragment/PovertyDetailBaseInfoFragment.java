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
import com.dyhl.dusky.huangchuanfp.Adapter.AnnuncementAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;

import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyDetail;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class PovertyDetailBaseInfoFragment extends BaseFragment {
    private String type;
    List<BaseInfo> datas;
    String idcard;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.tip)
    TextView tip;
    PovertyBaseInfoAdapter adapter;
    protected ProgressDialog dialog;

    public static PovertyDetailBaseInfoFragment newInstance(String type,String id) {
        PovertyDetailBaseInfoFragment fragment=  new PovertyDetailBaseInfoFragment();
        fragment.type=type;
        fragment.idcard=id;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_poverty_baseinfo;
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
        adapter=new PovertyBaseInfoAdapter(datas,getActivity());
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    public void loadData(){
        dialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        RetrofitHelper.getPovertyListAPI()
                .getData(idcard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            PovertyInformation povertyInformation= JSON.parseObject(apiMsg.getResult(),PovertyInformation.class);
                            if(povertyInformation==null){
                                tip.setHint("没有查询到相关信息");
                                return;
                            }
                            //{"idcard":"413024194012146313","phone":"15978392579","nation":"汉族","tpnd":"2017","position":"","id":"ffc1f0a1649d11e89370e0d55e42430b","reson":"缺资金","dangerousbuilding":"否","education":"小学","peopleid":"310040497845","familyid":"3113335813","department":"","watersafety":"是","overcomeattribute":"已脱贫（享受政策）","worktime":"0","income":"3884.45","population":"6","dysdipsia":"否","povertyattribute":"一般农户","student":"非在校生","county":"","medical":"是","village":"王楼村","work":"","naturalvillage":"郑岗","wdtpnd":"","health":"长期慢性病","town":"双柳树镇","responsible":"","relationship":"户主","skill":"丧失劳动力","name":"王明甫","responsiblephone":""}
                            BaseInfo baseInfo0=new BaseInfo();
                            baseInfo0.setKey("证件号码");
                            baseInfo0.setValue(povertyInformation.getIdcard());
                            datas.add(baseInfo0);
                            BaseInfo baseInfo01=new BaseInfo();
                            baseInfo01.setKey("民族");
                            baseInfo01.setValue(povertyInformation.getNation());
                            datas.add(baseInfo01);
                            BaseInfo baseInfo02=new BaseInfo();
                            baseInfo02.setKey("文化程度");
                            baseInfo02.setValue(povertyInformation.getEducation());
                            datas.add(baseInfo02);
                            BaseInfo baseInfo06=new BaseInfo();
                            baseInfo06.setKey("在校生状况");
                            baseInfo06.setValue(povertyInformation.getStudent());
                            datas.add(baseInfo06);
                            BaseInfo baseInfo05=new BaseInfo();
                            baseInfo05.setKey("劳动技能");
                            baseInfo05.setValue(povertyInformation.getSkill());
                            datas.add(baseInfo05);
                            BaseInfo baseInfo04=new BaseInfo();
                            baseInfo04.setKey("家庭人数");
                            baseInfo04.setValue(povertyInformation.getPopulation());
                            datas.add(baseInfo04);
                            BaseInfo baseInfo03=new BaseInfo();
                            baseInfo03.setKey("人均纯收入");
                            baseInfo03.setValue(povertyInformation.getIncome());
                            datas.add(baseInfo03);
                            BaseInfo baseInfo07=new BaseInfo();
                            baseInfo07.setKey("务工状况");
                            baseInfo07.setValue(TextUtils.isEmpty(povertyInformation.getWork())?"无":povertyInformation.getWork());
                            datas.add(baseInfo07);
                            BaseInfo baseInfo08=new BaseInfo();
                            baseInfo08.setKey("务工时间(月)");
                            baseInfo08.setValue(TextUtils.isEmpty(povertyInformation.getWorktime())?"0":povertyInformation.getWorktime());
                            datas.add(baseInfo08);
                            BaseInfo baseInfo=new BaseInfo();
                            baseInfo.setKey("电话号码");
                            baseInfo.setValue(povertyInformation.getPhone());
                            datas.add(baseInfo);
                            BaseInfo baseInfo2=new BaseInfo();
                            baseInfo2.setKey("家庭住址");
                            baseInfo2.setValue(povertyInformation.getVillage()+povertyInformation.getNaturalvillage());
                            datas.add(baseInfo2);
                            BaseInfo baseInfo3=new BaseInfo();
                            baseInfo3.setKey("是否参加大病医疗");
                            baseInfo3.setValue(povertyInformation.getMedical());
                            datas.add(baseInfo3);
                            BaseInfo baseInfo4=new BaseInfo();
                            baseInfo4.setKey("致贫原因");
                            baseInfo4.setValue(povertyInformation.getReson());
                            datas.add(baseInfo4);
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
