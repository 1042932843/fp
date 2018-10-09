package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.ExMineFragment;
import com.dyhl.dusky.huangchuanfp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ExMineActivity extends BaseActivity {

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.toolbar_tab)
    TabLayout tabs;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exmine;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initViewPager();
    }

    @Override
    public void initToolBar() {

    }

    public void initViewPager(){
        List<String> titles;
        String[] PLANETS = {"全部","公开","被屏蔽"};
        titles= Arrays.asList(PLANETS);
        List<Fragment> mFragments=new ArrayList<>();
        ExMineFragment exMineFragment=ExMineFragment.newInstance("全部");
        ExMineFragment exMineFragment2=ExMineFragment.newInstance("公开");
        ExMineFragment exMineFragment3=ExMineFragment.newInstance("屏蔽");
        mFragments.add(exMineFragment);
        mFragments.add(exMineFragment2);
        mFragments.add(exMineFragment3);
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




}


