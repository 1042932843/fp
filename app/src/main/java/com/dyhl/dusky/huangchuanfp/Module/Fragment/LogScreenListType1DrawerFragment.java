package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter_lv3;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.CommonListActivity;
import com.dyhl.dusky.huangchuanfp.Module.LogScreenListActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.Town;
import com.dyhl.dusky.huangchuanfp.Module.entity.Village;
import com.dyhl.dusky.huangchuanfp.Module.entity.logCommonData;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LogScreenListType1DrawerFragment extends BaseFragment {
    logCommonData logCommonData;
    DrawerLayout drawer;
    String loc="";
    String xianName="潢川县";
    String townName="";
    String villageName="";

    @BindView(R.id.zrr_name)
    KeyEditText zrr_name;

    @OnClick(R.id.ok)
    public void OK(){
        loc="";
        logCommonData=new logCommonData();
        if(!TextUtils.isEmpty(zrr_name.getText().toString())){
            logCommonData.setName(zrr_name.getText().toString());
        }
        logCommonData.setCode(permissionstag);
        logCommonData.setType("1");
        if(!TextUtils.isEmpty(townName)){
            loc=xianName+","+townName;
        }
        if(!TextUtils.isEmpty(villageName)&&!villageName.equals("选择村")){
            loc=xianName+","+townName+","+villageName;
        }

        logCommonData.setLoc(loc);
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        String start=logCommonData.getStart();
        String end=logCommonData.getEnd();
        if(!TextUtils.isEmpty(start)&&!TextUtils.isEmpty(end)){
            Date sd = null;
            Date ed = null;
            try {
                sd = format.parse(start);
                ed =format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.print("Format To times:"+sd.getTime());
            System.out.print("Format To times:"+ed.getTime());
            if(sd.getTime()>ed.getTime()){
                ToastUtil.ShortToast("结束时间小于起始时间，请选择合理时间段");
                return;
            }
        }
        EventBus.getDefault().post(logCommonData);

        saixuan();
    }

    @OnClick(R.id.reset)
    public void reset(){
        loc="";
        townName="";
        villageName="";
        zrr_name.setText("");
        ad_lv2.setSelection(t_p2);
        ad_lv3.setSelection(v_p2);
    }

    public static LogScreenListType1DrawerFragment newInstance(DrawerLayout drawer) {
        LogScreenListType1DrawerFragment fragment=  new LogScreenListType1DrawerFragment();
        fragment.drawer=drawer;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_drawer_logtype1;

    }
    public void saixuan(){
        if(drawer.isDrawerOpen(Gravity.END)){
            drawer.closeDrawer(Gravity.END);
        }else{
            drawer.openDrawer(Gravity.END);
        }
    }

    @Override
    public void finishCreateView(Bundle state) {
        initAllTown();
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

    @BindView(R.id.xian)
    AppCompatSpinner ad_lv1;

    @BindView(R.id.zhen)
    AppCompatSpinner ad_lv2;

    @BindView(R.id.cun)
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
            townName=towns.get(position).getName();
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
            if(isFirst){
                isFirst=false;
            }else{

                //loadData();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };

    @SuppressLint("CheckResult")
    public void loadData(){

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}



