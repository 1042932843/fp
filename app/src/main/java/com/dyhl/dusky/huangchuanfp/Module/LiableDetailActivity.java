package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;

import com.dyhl.dusky.huangchuanfp.Base.BaseActivityForStatus;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LiableDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LiableLoglistFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LiablePovertyListInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailFamilyInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLiableInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLogInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
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

public class LiableDetailActivity extends BaseActivityForStatus {



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
    @BindView(R.id.bfhs)
    TextView bfhs;
    @BindView(R.id.zfl)
    TextView zfl;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.position)
    TextView position;

    Liable liable;

    @Override
    public int getLayoutId() {
        return R.layout.activity_liabledetail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        liable = (Liable) getIntent().getSerializableExtra("LiableInfo");
        // 绑定数据
        initWidget();
        if(liable!=null){
            name.setText(liable.getResponsible());
            bfhs.setText(liable.getBfpkh());
            zfl.setText(liable.getBfzfl());
            department.setText(liable.getDepartment());
            position.setText(liable.getPosition());
            initViewPager();
        }


    }

    @Override
    public void initToolBar() {

    }

    public void initViewPager(){
        List<String> titles;
        String[] PLANETS = {"基本信息","帮扶日志","贫困户"};
        titles= Arrays.asList(PLANETS);
        List<Fragment> mFragments=new ArrayList<>();
        LiableDetailBaseInfoFragment povertyDetailBaseInfoFragment= LiableDetailBaseInfoFragment.newInstance(titles.get(0),liable);
        LiableLoglistFragment povertyDetailFamilyInfoFragment= LiableLoglistFragment.newInstance(titles.get(1),liable);
        LiablePovertyListInfoFragment povertyDetailLiableInfoFragment= LiablePovertyListInfoFragment.newInstance(titles.get(2),liable);
        mFragments.add(povertyDetailBaseInfoFragment);
        mFragments.add(povertyDetailFamilyInfoFragment);
        mFragments.add(povertyDetailLiableInfoFragment);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, titles);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(titles.size());
        tabs.setupWithViewPager(view_pager);
        //tab可滚动
        //tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

    }

    @SuppressLint("CheckResult")
    public void loadData() {


    }


    private void initWidget() {
        setAppTitle("责任人详情");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
