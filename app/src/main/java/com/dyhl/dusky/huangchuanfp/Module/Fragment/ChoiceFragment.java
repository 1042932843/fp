package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;


import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.ChoiceAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter_lv3;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.MySpinner;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.Town;
import com.dyhl.dusky.huangchuanfp.Module.entity.Village;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChoiceFragment extends BaseFragment {
    public PovertyInformation Choice;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private List<PovertyInformation> home;

    String type;
    String xianName="潢川县";
    String townName="";
    String villageName="";
    int currentPage=1;
    ChoiceAdapter choiceAdapter;
    private boolean mIsRefreshing = false;

    @BindView(R.id.play_list)
    RecyclerView recyclerView;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @OnClick({R.id.container,R.id.img})
    public void doback(){
        getActivity().onBackPressed();
    }

    public static ChoiceFragment newInstance(String type) {
        ChoiceFragment fragment=  new ChoiceFragment();
        fragment.type=type;
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_play_queue;
    }


    @Override
    public void finishCreateView(Bundle state) {
        //EventBus.getDefault().post(Choice);
        Choice=new PovertyInformation();
        if("sign".equals(type)){
            layout.setVisibility(View.GONE);
        }
        initAllTown();
        home=new ArrayList<>();
        initRecyclerView();
        loadData();
    }

    public void initRecyclerView(){
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        choiceAdapter=new ChoiceAdapter(home,getActivity());
        recyclerView.setAdapter(choiceAdapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData();
            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();
        choiceAdapter.setOnItemClickListener(new ChoiceAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Choice=home.get(position);
                getActivity().onBackPressed();
                EventBus.getDefault().post(Choice);
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

    @SuppressLint("CheckResult")
    private void loadData() {
        mIsRefreshing=true;
        String id=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);
        RetrofitHelper.getChoiceFragmentAPI()
                .getPartnerInfo(id,currentPage+"",12+"")
                //.compose(this.bindToLifecycle())
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
                                home.add(JSON.parseObject(o, PovertyInformation.class));
                            }
                            choiceAdapter.notifyItemRangeChanged(((currentPage-1)*12+1),12);
                            currentPage++;

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


    ArrayList<Town> towns;
    ArrayList<Town> all;
    ArrayList<Village> villages;
    int t_p=0;
    int t_p2=0;
    int v_p=0;
    int v_p2=0;
    String towidtag;
    String permissionstag;

    SpinnerAdapter spinnerAdapter,spinnerAdapter2;
    SpinnerAdapter_lv3 spinnerAdapterlv3;

    @BindView(R.id.ad_lv1)
    MySpinner ad_lv1;

    @BindView(R.id.ad_lv2)
    AppCompatSpinner ad_lv2;

    @BindView(R.id.ad_lv3)
    AppCompatSpinner ad_lv3;

    @SuppressLint("CheckResult")
    private void initAllTown() {
        permissionstag="";
        towns =new ArrayList<>();
        all=new ArrayList<>();
        String permissions= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        Town townall=new Town();
        if(TextUtils.isEmpty(towidtag)){
            towidtag= PreferenceUtil.getStringPRIVATE("townid", UserState.NA);
        }
        if(TextUtils.isEmpty(permissionstag)){
            permissionstag= permissions;
        }

        RetrofitHelper.getCommonListAPI()
                .getArea(permissions)
                //.compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            String result=apiMsg.getResult();
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("towns");
                            for(int i=0;i<jsonArray.length();i++){
                                Town town=JSON.parseObject(jsonArray.get(i).toString(),Town.class);
                                if(town!=null){
                                    if(towidtag.equals(town.getCode())){
                                        t_p=i;
                                        t_p2=i;
                                    }
                                    towns.add(town);
                                    ArrayList<Village> vi=town.getVillages();
                                    for(int j=0;j<vi.size();j++){
                                        if(permissionstag.equals(vi.get(j).getCode())){
                                            v_p=j;
                                            v_p2=j;
                                        }
                                    }
                                    if(vi.size()>1){
                                        Village village=new Village();
                                        village.setName("");
                                        village.setCode(town.getCode());
                                        vi.add(0,village);
                                        town.setVillages(vi);
                                    }
                                }

                            }
                            if(towns.size()>1){
                                Town town=new Town();
                                town.setName("选择乡");
                                town.setCode(permissions);
                                ArrayList<Village> villages=new ArrayList<>();
                                Village village=new Village();
                                village.setCode(permissions);
                                village.setName("选择村");
                                villages.add(village);
                                town.setVillages(villages);
                                towns.add(0,town);
                            }
                            spinnerAdapter=new SpinnerAdapter(towns,getContext());
                            ad_lv2.setAdapter(spinnerAdapter);
                            townall.setName(jsonObject.getString("name"));
                            all.add(townall);
                            spinnerAdapter2=new SpinnerAdapter(all,getContext());
                            ad_lv1.setAdapter(spinnerAdapter2);
                            ad_lv2.setOnItemSelectedListener(onItemSelectedListener);
                            ad_lv2.setSelection(t_p);

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                }, throwable -> {

                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }


    private AdapterView.OnItemSelectedListener onItemSelectedListener= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //选择列表项的操作
            villages=towns.get(position).getVillages();
            towidtag=towns.get(position).getCode();
            Choice.setCode(towidtag);
            townName=towns.get(position).getName();
            Choice.setTown(townName);
            spinnerAdapterlv3=new SpinnerAdapter_lv3(villages,getContext());
            ad_lv3.setAdapter(spinnerAdapterlv3);
            ad_lv3.setOnItemSelectedListener(onItemSelectedListener2);
            if(position==t_p){
                ad_lv3.setSelection(v_p);
                t_p=-1;
                v_p=-1;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };

    boolean isFirst=true;
    private AdapterView.OnItemSelectedListener onItemSelectedListener2= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //选择列表项的操作
            //code= villages.get(position).getCode();
            permissionstag=villages.get(position).getCode();
            villageName=villages.get(position).getName();
            Choice.setVillage(villageName);
            Choice.setCode(permissionstag);

            if(isFirst){
                isFirst=false;
            }else{
                Choice.setName(Choice.getTown()+villageName);
                EventBus.getDefault().post(Choice);
                //loadData();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
