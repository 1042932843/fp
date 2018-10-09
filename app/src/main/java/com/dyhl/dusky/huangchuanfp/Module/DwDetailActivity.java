package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivityForStatus;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.DwLiableFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.DwPkcFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailPovertyListInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailZcgzdInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PkcDetailzrzInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailFamilyInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLiableInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLogInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.BFDWInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DwDetailActivity extends BaseActivityForStatus {

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
    @BindView(R.id.department)
    TextView department;

    @BindView(R.id.position)
    TextView phone;

    @OnClick(R.id.position)
    public void show(){
        //ShowPopWindow(phone);
    }



    BFDWInfo bfdwInfo;
    @Override
    public int getLayoutId() {
        return R.layout.activity_dwdetail;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initViews(Bundle savedInstanceState) {
        bfdwInfo = (BFDWInfo) getIntent().getSerializableExtra("item");
        // 绑定数据
        if(bfdwInfo!=null){
            name.setText(bfdwInfo.getDepartment());
            bfhs.setText(bfdwInfo.getIncharge());
            department.setText(bfdwInfo.getContact());
            phone.setText(bfdwInfo.getPhone());
            initViewPager();
        }
        initWidget();
    }

    @Override
    public void initToolBar() {

    }

    public void initViewPager(){
        List<String> titles;
        String[] PLANETS = {"帮扶责任人列表","结对村"};
        titles= Arrays.asList(PLANETS);
        List<Fragment> mFragments=new ArrayList<>();
        DwLiableFragment dwLiableFragment=DwLiableFragment.newInstance(bfdwInfo);
        mFragments.add(dwLiableFragment);
        DwPkcFragment dwPkcFragment=DwPkcFragment.newInstance(bfdwInfo);
        mFragments.add(dwPkcFragment);
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
        setAppTitle("单位详情");

    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
