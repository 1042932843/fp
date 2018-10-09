package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter_lv3;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
import com.dyhl.dusky.huangchuanfp.Module.entity.AnnuncementCommonData;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.ExCommonData;
import com.dyhl.dusky.huangchuanfp.Module.entity.Town;
import com.dyhl.dusky.huangchuanfp.Module.entity.Village;
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

public class AnnunDrawerFragment extends BaseFragment {
    DrawerLayout drawer;
    String type;
    String xianName="潢川县";
    String townName="";
    String villageName="";

    AnnuncementCommonData logCommonData;
    String code;
    @BindView(R.id.outp)
    Switch outp;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.title)
    KeyEditText title;





    @BindView(R.id.publicslayout)
    RelativeLayout publicslayout;

    boolean reset=false;
    @OnClick(R.id.ok)
    public void OK(){
       /* if(reset){
            logCommonData.setCode("");
        }else{
            logCommonData.setCode(permissionstag);
        }*/
        //logCommonData.setV_source(name.getText().toString());
        logCommonData.setV_title(title.getText().toString());
        reset=false;
        if(!checkBox.isChecked()){
            logCommonData.setV_publics("");
        }else{
            if(outp.isChecked()){
                logCommonData.setV_publics("公开");
            }else{
                logCommonData.setV_publics("屏蔽");
            }
        }
        logCommonData.setV_code(permissionstag);
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        String start=logCommonData.getV_start();
        String end=logCommonData.getV_end();
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
        if(start!=null){
            start.setText("");
            start.setHint("请选择");
        }
        if(end!=null){
            end.setText("");
            end.setHint("请选择");
        }
        if(title!=null){
            title.setText("");
        }
        if(checkBox!=null){
            checkBox.setChecked(false);
        }
        townName="";
        villageName="";
        ad_lv2.setSelection(t_p2);
        ad_lv3.setSelection(v_p2);
        reset=true;
        logCommonData=new AnnuncementCommonData();
    }

    public void saixuan(){
        if(drawer.isDrawerOpen(Gravity.END)){
            drawer.closeDrawer(Gravity.END);
        }else{
            drawer.openDrawer(Gravity.END);
        }
    }

    public static AnnunDrawerFragment newInstance(DrawerLayout drawer,String type) {
        AnnunDrawerFragment fragment=  new AnnunDrawerFragment();
        fragment.drawer=drawer;
        fragment.type=type;
        fragment.code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.layout_drawer_annuncedrawer;

    }

    @BindView(R.id.quyu)
    LinearLayout quyu;

    @Override
    public void finishCreateView(Bundle state) {
        if("notice".equals(type)){
            quyu.setVisibility(View.VISIBLE);
        }else{
            quyu.setVisibility(View.GONE);
        }
        initAllTown();
        if(code.length()<=6){
            publicslayout.setVisibility(View.VISIBLE);
        }
        logCommonData=new AnnuncementCommonData();
        start=(Button) parentView.findViewById(R.id.start);
        end=(Button) parentView.findViewById(R.id.end);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 调用时间选择器
                new DatePickerDialog(getActivity(), onDateStartSetListener, mYear, mMonth, mDay).show();

            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 调用时间选择器
                new DatePickerDialog(getActivity(), onDateEndSetListener, mYear, mMonth, mDay).show();
            }
        });
        getDate();

        outp.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                outp.setEnabled(isChecked);
                if(isChecked==false){
                    outp.setChecked(false);
                    text.setTextColor(ContextCompat.getColor(getContext(),R.color.text3));
                }else{
                    text.setTextColor(ContextCompat.getColor(getContext(),R.color.text2));
                }
            }
        });
    }


    @SuppressLint("CheckResult")
    public void loadData(){

    }

    /**
     * 日期选择器对话框监听
     */

    int mYear;
    int mMonth;
    int mDay;
    Button start,end;
    String startTime;
    String endTime;
    private void getDate(){
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }
    private DatePickerDialog.OnDateSetListener onDateStartSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    startTime = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    startTime = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    startTime = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    startTime = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            logCommonData.setV_start(startTime);
            start.setHint(startTime);
        }
    };
    private DatePickerDialog.OnDateSetListener onDateEndSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    endTime = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    endTime = new StringBuffer().append(mYear).append("-").append("0").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            } else {
                if (mDay < 10) {
                    endTime = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append("0").append(mDay).toString();
                } else {
                    endTime = new StringBuffer().append(mYear).append("-").
                            append(mMonth + 1).append("-").append(mDay).toString();
                }

            }
            logCommonData.setV_end(endTime);
            end.setHint(endTime);
        }
    };


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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}