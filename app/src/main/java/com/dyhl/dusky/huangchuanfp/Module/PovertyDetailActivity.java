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
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivityForStatus;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailBaseInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailFamilyInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLiableInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.PovertyDetailLogInfoFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
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

public class PovertyDetailActivity extends BaseActivityForStatus {

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
    @BindView(R.id.pkst)
    TextView pkst;
    @BindView(R.id.pktype)
    TextView pktype;
    @BindView(R.id.pkres)
    TextView pkres;
    @BindView(R.id.address)
    TextView address;

    String idcard;
    String familyid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_povertydetail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        PovertyInformation person = (PovertyInformation) getIntent().getSerializableExtra("PovertyInfo");
        // 绑定数据
        if(person!=null){
            name.setText(person.getName());
            String stu=person.getOvercomeattribute();
            pkst.setText(stu);
            if(stu.contains("未脱")){
                pkst.setBackground(getResources().getDrawable(R.drawable.btn_tip_red_solid_bg));
            }else{
                pkst.setBackground(getResources().getDrawable(R.drawable.btn_tip_green_solid_bg));
            }
            pktype.setText(person.getPovertyattribute());
            pkres.setText(person.getReson());
            String add=person.getCounty()+person.getTown()+person.getVillage();
            address.setText(add);
            idcard=person.getIdcard();
            familyid=person.getFamilyid();
        }

        initWidget();
        initViewPager();
    }

    @Override
    public void initToolBar() {

    }

    public void initViewPager(){
        List<String> titles;
        String[] PLANETS = {"基本信息","家庭成员","帮扶责任人","帮扶日志"};
        titles= Arrays.asList(PLANETS);
        List<Fragment> mFragments=new ArrayList<>();
        PovertyDetailBaseInfoFragment povertyDetailBaseInfoFragment= PovertyDetailBaseInfoFragment.newInstance(titles.get(0),idcard);
        PovertyDetailFamilyInfoFragment povertyDetailFamilyInfoFragment= PovertyDetailFamilyInfoFragment.newInstance(titles.get(1),familyid);
        PovertyDetailLiableInfoFragment povertyDetailLiableInfoFragment= PovertyDetailLiableInfoFragment.newInstance(titles.get(2),familyid);
        PovertyDetailLogInfoFragment povertyDetailLogInfoFragment= PovertyDetailLogInfoFragment.newInstance(titles.get(3),idcard);
        mFragments.add(povertyDetailBaseInfoFragment);
        mFragments.add(povertyDetailFamilyInfoFragment);
        mFragments.add(povertyDetailLiableInfoFragment);
        mFragments.add(povertyDetailLogInfoFragment);

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
        setAppTitle("贫困户详情");

    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
