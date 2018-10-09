package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;

import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LogByLiableFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LogByMonthFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LogByTownFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LogByVillageFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LogScreenListType1DrawerFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.LogScreenListType3DrawerFragment;

import com.dyhl.dusky.huangchuanfp.Module.entity.logCommonData;
import com.dyhl.dusky.huangchuanfp.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LogScreenListActivity extends BaseActivity {

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

    @BindView(R.id.drawer)
    DrawerLayout drawer;


    String code="";
    @Override
    public int getLayoutId() {
        return R.layout.activity_logscreen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // 绑定数据
        initWidget();
        initViewPager();
        initFragment();

    }


    logCommonData logCommonData;

    @Override
    public void initToolBar() {

    }

    public void initViewPager(){
        List<String> titles;
        String[] PLANETS = {"按月份统计","按责任人统计"};
        titles= Arrays.asList(PLANETS);
        List<Fragment> mFragments=new ArrayList<>();
        LogByMonthFragment logByMonthFragment= LogByMonthFragment.newInstance("1",drawer);
        mFragments.add(logByMonthFragment);
        LogByLiableFragment logByLiableFragment= LogByLiableFragment.newInstance("3",drawer);
        mFragments.add(logByLiableFragment);
        LogByVillageFragment logByVillageFragment= LogByVillageFragment.newInstance("4");
        //mFragments.add(logByVillageFragment);
        LogByTownFragment logByTownFragment= LogByTownFragment.newInstance("5");
        //mFragments.add(logByTownFragment);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mFragments, titles);
        view_pager.setAdapter(adapter);
        view_pager.setOffscreenPageLimit(titles.size());
        tabs.setupWithViewPager(view_pager);
        //tab可滚动
        tabs.setTabMode(TabLayout.MODE_FIXED);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(fragmentArrayList.size()>position){
                    changeTab(position);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private int currentIndex = 0;
    private ArrayList<Fragment> fragmentArrayList;
    private Fragment mCurrentFrgment;

    private void initFragment() {
        fragmentArrayList = new ArrayList<>();
        LogScreenListType1DrawerFragment listType1DrawerFragment=LogScreenListType1DrawerFragment.newInstance(drawer);
        LogScreenListType3DrawerFragment listType3DrawerFragment=LogScreenListType3DrawerFragment.newInstance(drawer);
        fragmentArrayList.add(listType1DrawerFragment);
        fragmentArrayList.add(listType3DrawerFragment);

        changeTab(0);
    }

    private void changeTab(int index) {
        currentIndex = index;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //判断当前的Fragment是否为空，不为空则隐藏
        if (null != mCurrentFrgment) {
            ft.hide(mCurrentFrgment);
        }
        //先根据Tag从FragmentTransaction事物获取之前添加的Fragment
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentArrayList.get(currentIndex).getClass().getName());

        if (null == fragment) {
            //如fragment为空，则之前未添加此Fragment。便从集合中取出
            fragment = fragmentArrayList.get(index);
        }
        mCurrentFrgment = fragment;

        //判断此Fragment是否已经添加到FragmentTransaction事物中
        if (!fragment.isAdded()) {
            ft.add(R.id.container, fragment, fragment.getClass().getName());
        } else {
            ft.show(fragment);
        }
        ft.commit();
    }


    @SuppressLint("CheckResult")
    public void loadData() {

    }




    private void initWidget() {
        setAppTitle("日志统计");
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
