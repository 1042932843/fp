package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;

import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter_lv3;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.MySpinner;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.StatisticsDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.StatisticsDetailDataFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.StatisticsDetailOutWorkFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.StatisticsDetailReasonFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.StatisticsDetailTypeFragment;
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
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StatisticsDetailActivity extends BaseActivity {
    protected ProgressDialog dialog;
    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;

    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.toolbar_tab)
    TabLayout tabs;

    @BindView(R.id.ad_lv1)
    MySpinner ad_lv1;

    @BindView(R.id.ad_lv2)
    AppCompatSpinner ad_lv2;

    @BindView(R.id.ad_lv3)
    AppCompatSpinner ad_lv3;

    SpinnerAdapter spinnerAdapter,spinnerAdapter2;
    SpinnerAdapter_lv3 spinnerAdapterlv3;

    String code="";
    @Override
    public int getLayoutId() {
        return R.layout.activity_statisicsdetail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        dialog.show();
        // 绑定数据
        initWidget();
        initViewPager();
    }

    @Override
    public void initToolBar() {

    }

    public void initViewPager(){
        List<String> titles;
        String[] PLANETS = {"扶贫概况","致贫原因","贫困户类型","历年脱贫","外出务工情况"};
        titles= Arrays.asList(PLANETS);
        List<Fragment> mFragments=new ArrayList<>();
        StatisticsDetailBaseInfoFragment statisticsDetailBaseInfoFragment=StatisticsDetailBaseInfoFragment.newInstance();
        StatisticsDetailReasonFragment statisticsDetailReasonFragment=StatisticsDetailReasonFragment.newInstance();
        StatisticsDetailTypeFragment statisticsDetailTypeFragment=StatisticsDetailTypeFragment.newInstance();
        StatisticsDetailDataFragment statisticsDetailDataFragment= StatisticsDetailDataFragment.newInstance();
        StatisticsDetailOutWorkFragment statisticsDetailOutWorkFragment= StatisticsDetailOutWorkFragment.newInstance();
        mFragments.add(statisticsDetailBaseInfoFragment);
        mFragments.add(statisticsDetailReasonFragment);
        mFragments.add(statisticsDetailTypeFragment);
        mFragments.add(statisticsDetailDataFragment);
        mFragments.add(statisticsDetailOutWorkFragment);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, titles);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(titles.size());
        tabs.setupWithViewPager(view_pager);
        //tab可滚动
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @SuppressLint("CheckResult")
    public void loadData() {
        initAllTown();

    }



    ArrayList<Town> towns;
    ArrayList<Town> all;
    ArrayList<Village> villages;
    int a_p=0;
    int t_p=0;
    int v_p=0;
    String towidtag;
    public static String permissionstag;
    @SuppressLint("CheckResult")
    private void initAllTown() {
        String permissions= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        towns =new ArrayList<>();
        all=new ArrayList<>();

        Town townall=new Town();
        if(TextUtils.isEmpty(towidtag)){
            towidtag= PreferenceUtil.getStringPRIVATE("townid", UserState.NA);
        }
        if(TextUtils.isEmpty(permissionstag)){
            permissionstag= permissions;
        }

        RetrofitHelper.getCommonListAPI()
                .getArea(PreferenceUtil.getStringPRIVATE("permissions", ""))
                .compose(this.bindToLifecycle())
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
                                        if(i>1){
                                            t_p=i+1;//+1是因为白板项
                                        }else{
                                            t_p=i;
                                        }

                                    }
                                    towns.add(town);
                                    ArrayList<Village> vi=town.getVillages();
                                    for(int j=0;j<vi.size();j++){
                                        if(permissionstag.equals(vi.get(j).getCode())){
                                            if(j>1){
                                                v_p=j+1;
                                            }else{
                                                v_p=j;
                                            }

                                        }
                                    }
                                    if(vi.size()>1){
                                        Village village=new Village();
                                        village.setName("选择村");
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

                            townall.setName(jsonObject.getString("name"));
                            all.add(townall);
                            spinnerAdapter2=new SpinnerAdapter(all,this);
                            ad_lv1.setAdapter(spinnerAdapter2);
                            ad_lv1.setOnItemSelectedListener(onItemSelectedListener0);

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                    dialog.dismiss();
                }, throwable -> {
                    dialog.dismiss();
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener0= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spinnerAdapter=new SpinnerAdapter(towns,StatisticsDetailActivity.this);
            ad_lv2.setAdapter(spinnerAdapter);
            ad_lv2.setOnItemSelectedListener(onItemSelectedListener);
            if(position==a_p){
                a_p=-1;
                if(spinnerAdapter.getCount()>t_p){
                    ad_lv2.setSelection(t_p);
                }

            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //选择列表项的操作
            villages=towns.get(position).getVillages();
            towidtag=towns.get(position).getCode();
            spinnerAdapterlv3=new SpinnerAdapter_lv3(villages,StatisticsDetailActivity.this);
            ad_lv3.setAdapter(spinnerAdapterlv3);
            ad_lv3.setOnItemSelectedListener(onItemSelectedListener2);
            if(position==t_p){
                if(spinnerAdapterlv3.getCount()>v_p){
                    ad_lv3.setSelection(v_p);
                }

                t_p=-1;
                v_p=-1;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener2= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //选择列表项的操作
            code= villages.get(position).getCode();
            permissionstag=villages.get(position).getCode();
            EventBus.getDefault().post("StatisticsDetailBaseInfoFragment");
            EventBus.getDefault().post("StatisticsDetailReasonFragment");
            EventBus.getDefault().post("StatisticsDetailTypeFragment");
            EventBus.getDefault().post("StatisticsDetailDataFragment");
            EventBus.getDefault().post("StatisticsDetailOutWorkFragment");

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };

    private void initWidget() {
        setAppTitle("数据分析");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
