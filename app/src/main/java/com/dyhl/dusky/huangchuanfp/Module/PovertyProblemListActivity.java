package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.AnnuncementAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.FragmentAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.ProblemAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.ExMineFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyProblemFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Module.entity.Problem;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PovertyProblemListActivity extends BaseActivity {
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
        return R.layout.activity_problemlist;
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
        PovertyProblemFragment exMineFragment=PovertyProblemFragment.newInstance("全部");
        PovertyProblemFragment exMineFragment2= PovertyProblemFragment.newInstance("公开");
        PovertyProblemFragment exMineFragment3=PovertyProblemFragment.newInstance("屏蔽");
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
