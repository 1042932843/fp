package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;

import com.dyhl.dusky.huangchuanfp.Base.BaseActivityForStatus;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailPovertyListInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailZcgzdInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailzrzInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailFamilyInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLiableInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLogInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PkcDetailActivity extends BaseActivityForStatus {

    protected List<Fragment> mListFragment = new ArrayList<Fragment>();

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
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.pkcfzr)
    TextView pkcfzr;
    @BindView(R.id.pkcphone)
    TextView pkcphone;

    PkcInfo pkcInfo;
    @Override
    public int getLayoutId() {
        return R.layout.activity_pkcdetail;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        pkcInfo = (PkcInfo) getIntent().getSerializableExtra("PkcInfo");
        // 绑定数据
        if(pkcInfo!=null){
            name.setText(pkcInfo.getVillage());
            pkcfzr.setText(pkcInfo.getIncharge());
            pkcphone.setText(pkcInfo.getPhone());
            initViewPager();
        }
        initWidget();
    }

    @Override
    public void initToolBar() {

    }

    public void initViewPager(){
        List<String> titles;
        String[] PLANETS = {"基本信息","驻村工作队","扶贫攻坚责任组","贫困户"};
        titles= Arrays.asList(PLANETS);
        List<Fragment> mFragments=new ArrayList<>();
        PkcDetailBaseInfoFragment pkcDetailBaseInfoFragment=PkcDetailBaseInfoFragment.newInstance(pkcInfo);
        mFragments.add(pkcDetailBaseInfoFragment);
        PkcDetailZcgzdInfoFragment pkcDetailZcgzdInfoFragment=PkcDetailZcgzdInfoFragment.newInstance(pkcInfo);
        mFragments.add(pkcDetailZcgzdInfoFragment);
        PkcDetailzrzInfoFragment pkcDetailzrzInfoFragment=PkcDetailzrzInfoFragment.newInstance(pkcInfo);
        mFragments.add(pkcDetailzrzInfoFragment);
        PkcDetailPovertyListInfoFragment pkcDetailPovertyListInfoFragment=PkcDetailPovertyListInfoFragment.newInstance(pkcInfo);
        mFragments.add(pkcDetailPovertyListInfoFragment);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, titles);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(titles.size());
        tabs.setupWithViewPager(view_pager);
        //tab可滚动
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    @SuppressLint("CheckResult")
    public void loadData() {

    }


    private void initWidget() {
        setAppTitle("贫困村详情");

    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
