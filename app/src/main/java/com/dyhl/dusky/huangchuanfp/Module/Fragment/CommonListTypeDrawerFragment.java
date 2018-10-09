package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.flexbox.StringTagAdapter;
import com.dyhl.dusky.huangchuanfp.Design.flexbox.interfaces.OnFlexboxSubscribeListener;
import com.dyhl.dusky.huangchuanfp.Design.flexbox.widget.TagFlowLayout;
import com.dyhl.dusky.huangchuanfp.Design.keyEditText.KeyEditText;
import com.dyhl.dusky.huangchuanfp.Module.CommonListActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.CommonData;
import com.dyhl.dusky.huangchuanfp.Module.entity.OverPovertyType;
import com.dyhl.dusky.huangchuanfp.Module.entity.PoertyReson;
import com.dyhl.dusky.huangchuanfp.Module.entity.PoertyType;
import com.dyhl.dusky.huangchuanfp.Module.entity.Problem;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommonListTypeDrawerFragment extends BaseFragment {
    private int type;
    CommonData commonData;
    KeyEditText dysj_name;
    KeyEditText dysj_phone;

    KeyEditText bfdw_name;
    KeyEditText bfdw_fzr;
    KeyEditText bfdw_lxr;
    KeyEditText bfdw_lxdh;

    KeyEditText pkh_name;
    KeyEditText wtsb_name;
    KeyEditText wtsb_duixiang;
    KeyEditText zrr_name;
    KeyEditText pkh_responsible;
    KeyEditText zrr_danwei;
    KeyEditText zrr_phone;
    KeyEditText rz_name;
    KeyEditText rz_pkhname;
    KeyEditText pkc_village;
    Switch switch_out;
    CheckBox checkBox;
    CheckBox checkBoxr;
    TextView text;
    TextView textr;

    @OnClick(R.id.ok)
    public void OK(){
        switch (type){
            case R.id.main2_pkc:
                //type="贫困村";
                commonData.setVillage(pkc_village.getText().toString());
                String is=switch_out.isChecked()?"是":"否";
                if(checkBox.isChecked()){
                    commonData.setOutpoverty(is);
                }else{
                    commonData.setOutpoverty("");
                }

                break;
            case R.id.main2_pkh:
                //type="贫困户";
                commonData.setName(pkh_name.getText().toString());
                commonData.setResponsible(pkh_responsible.getText().toString());
                break;
            case R.id.main2_bfzrr:
                //type="帮扶责任人";
                commonData.setResponsible(zrr_name.getText().toString());
                commonData.setPosition(zrr_danwei.getText().toString());
                commonData.setPhone(zrr_phone.getText().toString());
                break;
            case R.id.main2_bfrz:
                //type="帮扶日志";
                commonData.setName(rz_pkhname.getText().toString());
                commonData.setResponsible(rz_name.getText().toString());
                if(!checkBox.isChecked()){
                    commonData.setPublics("");
                }else{
                    if(outp.isChecked()){
                        commonData.setPublics("公开");
                    }else{
                        commonData.setPublics("屏蔽");
                    }
                }
                break;
            case R.id.main2_sbwt:
                //type="上报问题";
                commonData.setName(wtsb_duixiang.getText().toString());
                commonData.setResponsible(wtsb_name.getText().toString());

                if(!checkBoxr.isChecked()){
                    commonData.setReply("");
                }else{
                    if(outpr.isChecked()){
                        commonData.setReply("已回复");
                    }else{
                        commonData.setReply("未回复");
                    }
                }

                if(!checkBox.isChecked()){
                    commonData.setPublics("");
                }else{
                    if(outp.isChecked()){
                        commonData.setPublics("公开");
                    }else{
                        commonData.setPublics("屏蔽");
                    }
                }
                break;

            case R.id.main2_bfdw:
                //type="帮扶单位";
                commonData.setDepartmentname(bfdw_name.getText().toString());
                commonData.setIncharge(bfdw_fzr.getText().toString());
                commonData.setContact(bfdw_lxr.getText().toString());
                commonData.setPhone(bfdw_lxdh.getText().toString());
                break;
            case R.id.main2_dysj:
                //type="第一书记";
                commonData.setName(dysj_name.getText().toString());
                commonData.setPhone(dysj_phone.getText().toString());
                break;
            case R.id.main2_gzd:
                //type="工作队";
                commonData.setName(dysj_name.getText().toString());
                commonData.setPhone(dysj_phone.getText().toString());
                break;
            case R.id.main2_zrz:
                //type="责任组";
                commonData.setName(dysj_name.getText().toString());
                commonData.setPhone(dysj_phone.getText().toString());
                break;
        }


        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        String start=commonData.getStart();
        String end=commonData.getEnd();
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
        EventBus.getDefault().post(commonData);
    }

    @OnClick(R.id.reset)
    public void reset(){
        commonData=new CommonData();
        if(selectItems!=null){
            selectItems.clear();
        }
        if(selectItems2!=null){
            selectItems2.clear();
        }
        if(selectItems3!=null){
            selectItems3.clear();
        }

        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        if(adapter2!=null){
            adapter2.notifyDataSetChanged();
        }
        if(adapter3!=null){
            adapter3.notifyDataSetChanged();
        }
        if(start!=null){
            start.setHint("请选择");
        }
        if(end!=null){
            end.setHint("请选择");
        }

        if(pkh_name!=null){
            pkh_name.setText("");
        }
        if(wtsb_name!=null){
            wtsb_name.setText("");
        }
        if(wtsb_duixiang!=null){
            wtsb_duixiang.setText("");
        }
        if(zrr_name!=null){
            zrr_name.setText("");
        }
        if(zrr_danwei!=null){
            zrr_danwei.setText("");
        }
        if(zrr_phone!=null){
            zrr_phone.setText("");
        }
        if(rz_name!=null){
            rz_name.setText("");
        }
        if(rz_pkhname!=null){
            rz_pkhname.setText("");
        }
        if(bfdw_lxdh!=null){
            bfdw_lxdh.setText("");
        }
        if(bfdw_lxr!=null){
            bfdw_lxr.setText("");
        }
        if(bfdw_fzr!=null){
            bfdw_fzr.setText("");
        }
        if(bfdw_name!=null){
            bfdw_name.setText("");
        }
        if(dysj_name!=null){
            dysj_name.setText("");
        }
        if(pkh_responsible!=null){
            pkh_responsible.setText("");
        }
        if(dysj_phone!=null){
            dysj_phone.setText("");
        }
        if(checkBox!=null){
            checkBox.setChecked(false);
        }
        if(checkBoxr!=null){
            checkBoxr.setChecked(false);
        }

    }

    public static CommonListTypeDrawerFragment newInstance(int position) {
        CommonListTypeDrawerFragment fragment=  new CommonListTypeDrawerFragment();
        fragment.type=position;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        switch (type){
            case R.id.main2_pkc:
                //type="贫困村";
                return R.layout.layout_drawer_pkc;
            case R.id.main2_pkh:
                //type="贫困户";
                return R.layout.layout_drawer_pkh;
            case R.id.main2_bfzrr:
                //type="帮扶责任人";
                return R.layout.layout_drawer_zrr;
            case R.id.main2_bfrz:
                //type="帮扶日志";
                return R.layout.layout_drawer_bfrz;
            case R.id.main2_sbwt:
                //type="上报问题";
                return R.layout.layout_drawer_sbwt;
            case R.id.main2_bfdw:
                //type="帮扶单位";
                return R.layout.layout_drawer_bfdw;
            case R.id.main2_dysj:
                //type="第一书记";
                return R.layout.layout_drawer_dysj;
            case R.id.main2_gzd:
                //type="工作队";
                return R.layout.layout_drawer_gzd;
            case R.id.main2_zrz:
                //type="责任组";
                return R.layout.layout_drawer_zrz;
            default:
                return R.layout.layout_drawer_bfrz;

        }

    }


    @Override
    public void finishCreateView(Bundle state) {
        commonData=new CommonData();
        getDate();
        switch (type){
            case R.id.main2_pkc:
                //type="贫困村";
                initPKC();
                break;
            case R.id.main2_pkh:
                //type="贫困户";
                initPKH();
                break;
            case R.id.main2_bfzrr:
                //type="帮扶责任人";
                initZRR();
                break;
            case R.id.main2_bfrz:
                //type="帮扶日志";
                initBFRZ();
                break;
            case R.id.main2_sbwt:
                //type="上报问题";
                initSBWT();
                break;
            case R.id.main2_bfdw:
                //type="帮扶单位";
                initBFDW();
                break;
            case R.id.main2_dysj:
                //type="第一书记";
                initDYSJ();
                break;
            case R.id.main2_zrz:
                //type="责任组";
                initZRZ();
                break;
            case R.id.main2_gzd:
                //type="工作队";
                initGZD();
                break;

        }
    }

    /**
     * drawer通用区域
     */
    Button start,end;
    TagFlowLayout flowLayout,flowLayout2,flowLayout3;

    private List<String> sourceData,sourceData2,sourceData3;
    private StringTagAdapter adapter,adapter2,adapter3;
    private List<String> selectItems,selectItems2,selectItems3;
    int mYear;
    int mMonth;
    int mDay;

    String startTime;
    String endTime;
    private void getDate(){
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }
    private void initflowLayoutData1() {
        sourceData = new ArrayList<>();
        selectItems = new ArrayList<>();
       /* sourceData.add("全部");
        sourceData.add("最近7天");
        sourceData.add("最近30天");
        sourceData.add("最近3个月");*/

    }
    @SuppressLint("CheckResult")
    private void initflowLayoutData2() {
        sourceData = new ArrayList<>();
        selectItems = new ArrayList<>();
        RetrofitHelper.getCommonListAPI()
                .getoverpovertyType("")
                //.compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONArray jsonArray = new JSONArray(apiMsg.getResult());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                OverPovertyType poertyType= JSON.parseObject(o, OverPovertyType.class);
                                sourceData.add(poertyType.getOvercomeattribute());
                            }
                            if(adapter!=null){
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                }, throwable -> {

                });

    }
    @SuppressLint("CheckResult")
    private void initflowLayoutData3() {
        sourceData = new ArrayList<>();
        sourceData2 = new ArrayList<>();
        sourceData3 = new ArrayList<>();
        selectItems = new ArrayList<>();
        selectItems2 = new ArrayList<>();
        selectItems3 = new ArrayList<>();
        RetrofitHelper.getCommonListAPI()
                .getoverpovertyType("")
                //.compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONArray jsonArray = new JSONArray(apiMsg.getResult());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                OverPovertyType poertyType= JSON.parseObject(o, OverPovertyType.class);
                                sourceData.add(poertyType.getOvercomeattribute());
                            }
                            if(adapter!=null){
                                adapter.notifyDataSetChanged();
                            }else{
                                adapter = new StringTagAdapter(getActivity(), sourceData, selectItems);
                            }
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                }, throwable -> {

                });



        RetrofitHelper.getCommonListAPI()
                .getPoertyType("")
                //.compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONArray jsonArray = new JSONArray(apiMsg.getResult());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                PoertyType poertyType= JSON.parseObject(o, PoertyType.class);
                                sourceData2.add(poertyType.getPovertyattribute());
                            }
                            if(adapter2!=null){
                                adapter2.notifyDataSetChanged();
                            }else{
                                adapter2 = new StringTagAdapter(getActivity(), sourceData2, selectItems2);
                            }
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                }, throwable -> {

                });


        RetrofitHelper.getCommonListAPI()
                .getPovertyReson("")
                //.compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONArray jsonArray = new JSONArray(apiMsg.getResult());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                PoertyReson poertyType= JSON.parseObject(o, PoertyReson.class);
                                sourceData3.add(poertyType.getReson());
                            }
                            if(adapter3!=null){
                                adapter3.notifyDataSetChanged();
                            }else{
                                adapter3 = new StringTagAdapter(getActivity(), sourceData3, selectItems3);
                            }
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                }, throwable -> {

                });
    }

    /**
     * 日期选择器对话框监听
     */
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
            commonData.setStart(startTime);
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
            commonData.setEnd(endTime);
            end.setHint(endTime);
        }
    };


    /**
     * 问题上报筛选模块
     */
    Switch outp;
    Switch outpr;
    private void initSBWT() {
        RelativeLayout  publicslayout=(RelativeLayout)parentView.findViewById(R.id.publicslayout);
        checkBox=(CheckBox) parentView.findViewById(R.id.checkBox) ;
        checkBoxr=(CheckBox) parentView.findViewById(R.id.checkBoxr);
        outp=(Switch) parentView.findViewById(R.id.outp) ;
        outpr=(Switch) parentView.findViewById(R.id.outpr) ;
        text=(TextView)parentView.findViewById(R.id.text) ;
        textr=(TextView)parentView.findViewById(R.id.textr) ;
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

        outpr.setEnabled(false);
        checkBoxr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                outpr.setEnabled(isChecked);
                if(isChecked==false){
                    outpr.setChecked(false);
                    textr.setTextColor(ContextCompat.getColor(getContext(),R.color.text3));
                }else{
                    textr.setTextColor(ContextCompat.getColor(getContext(),R.color.text2));
                }
            }
        });
        String code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        if(code.length()<=6){
            publicslayout.setVisibility(View.VISIBLE);
        }
        wtsb_name=(KeyEditText)parentView.findViewById(R.id.wtsb_name);
        wtsb_duixiang=(KeyEditText)parentView.findViewById(R.id.wtsb_duixiang);
        flowLayout = (TagFlowLayout) parentView.findViewById(R.id.flow);
        initflowLayoutData1();
        adapter = new StringTagAdapter(getActivity(), sourceData, selectItems);
        adapter.setOnSubscribeListener(new OnFlexboxSubscribeListener<String>() {
            @Override
            public void onSubscribe(List<String> selectedItem) {
               //ToastUtil.showShort(getContext(),"已选择" + selectedItem.size() + "个" + "\n" + "选中的是：" + selectedItem.toString());
            }
        });
        flowLayout.setAdapter(adapter);

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

    }

    /**
     * 帮扶日志筛选模块
     */
    private void initBFRZ() {
        rz_name=(KeyEditText) parentView.findViewById(R.id.rz_name);
        rz_pkhname=(KeyEditText) parentView.findViewById(R.id.rz_pkhname);
        flowLayout = (TagFlowLayout) parentView.findViewById(R.id.flow);
        text=(TextView)parentView.findViewById(R.id.text) ;
        checkBox=(CheckBox) parentView.findViewById(R.id.checkBox) ;
        outp=(Switch) parentView.findViewById(R.id.outp) ;
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
        String code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        RelativeLayout  publicslayout=(RelativeLayout)parentView.findViewById(R.id.publicslayout);
        if(code.length()<=6){
            publicslayout.setVisibility(View.VISIBLE);
        }
        initflowLayoutData1();
        adapter = new StringTagAdapter(getActivity(), sourceData, selectItems);
        adapter.setOnSubscribeListener(new OnFlexboxSubscribeListener<String>() {
            @Override
            public void onSubscribe(List<String> selectedItem) {

                //ToastUtil.showShort(getContext(),"已选择" + selectedItem.size() + "个" + "\n" + "选中的是：" + selectedItem.toString());
            }
        });
        flowLayout.setAdapter(adapter);

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

    }

    /**
     * 贫困村筛选模块
     */
    private void initPKC() {
        pkc_village=(KeyEditText)parentView.findViewById(R.id.village);
        switch_out=(Switch)parentView.findViewById(R.id.outp);
        checkBox=(CheckBox)parentView.findViewById(R.id.checkBox);
        text=(TextView)parentView.findViewById(R.id.text);
        text.setTextColor(ContextCompat.getColor(getContext(),R.color.text3));
        switch_out.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch_out.setEnabled(isChecked);
                if(isChecked==false){
                    switch_out.setChecked(false);
                    text.setTextColor(ContextCompat.getColor(getContext(),R.color.text3));
                }else{
                    text.setTextColor(ContextCompat.getColor(getContext(),R.color.text2));
                }
            }
        });
    }

    /**
     * 贫困户筛选模块
     */
    private void initPKH() {
        pkh_responsible=(KeyEditText)parentView.findViewById(R.id.pkh_responsible) ;
        flowLayout = (TagFlowLayout) parentView.findViewById(R.id.flow);
        flowLayout2 = (TagFlowLayout) parentView.findViewById(R.id.flow2);
        flowLayout3 = (TagFlowLayout) parentView.findViewById(R.id.flow3);
        pkh_name=(KeyEditText)parentView.findViewById(R.id.pkh_name) ;
        initflowLayoutData3();
        if(adapter==null){
            adapter = new StringTagAdapter(getActivity(), sourceData, selectItems);
        }
        if(adapter2==null){
            adapter2 = new StringTagAdapter(getActivity(), sourceData2, selectItems2);
        }
        if(adapter3==null){
            adapter3 = new StringTagAdapter(getActivity(), sourceData3, selectItems3);
        }

        adapter.setOnSubscribeListener(new OnFlexboxSubscribeListener<String>() {
            @Override
            public void onSubscribe(List<String> selectedItem) {
                if("全部".equals(selectedItem.get(0))){
                    commonData.setOverpvertyattribute("");
                }else{
                    commonData.setOverpvertyattribute(selectedItem.get(0));
                }
                //ToastUtil.showShort(getContext(),"已选择" + selectedItem.size() + "个" + "\n" + "选中的是：" + selectedItem.toString());
            }
        });
        adapter2.setOnSubscribeListener(new OnFlexboxSubscribeListener<String>() {
            @Override
            public void onSubscribe(List<String> selectedItem) {
                if("全部".equals(selectedItem.get(0))){
                    commonData.setPvertyattribute("");
                }else{
                    commonData.setPvertyattribute(selectedItem.get(0));
                }

                //ToastUtil.showShort(getContext(),"已选择" + selectedItem.size() + "个" + "\n" + "选中的是：" + selectedItem.toString());
            }
        });
        adapter3.setOnSubscribeListener(new OnFlexboxSubscribeListener<String>() {
            @Override
            public void onSubscribe(List<String> selectedItem) {
                if("全部".equals(selectedItem.get(0))){
                    commonData.setReson("");
                }else{
                    commonData.setReson(selectedItem.get(0));
                }

                //ToastUtil.showShort(getContext(),"已选择" + selectedItem.size() + "个" + "\n" + "选中的是：" + selectedItem.toString());
            }
        });
        flowLayout.setAdapter(adapter);
        flowLayout2.setAdapter(adapter2);
        flowLayout3.setAdapter(adapter3);

    }

    /**
     * 帮扶责任人筛选模块
     */
    private void initZRR() {
        zrr_name=(KeyEditText)parentView.findViewById(R.id.zrr_name) ;
        zrr_danwei=(KeyEditText)parentView.findViewById(R.id.zrr_danwei) ;
        zrr_phone=(KeyEditText)parentView.findViewById(R.id.zrr_phone) ;
    }

    /**
     * 帮扶单位筛选模块
     */
    public void initBFDW(){
        bfdw_name=(KeyEditText)parentView.findViewById(R.id.bfdw_name) ;
        bfdw_fzr=(KeyEditText)parentView.findViewById(R.id.bfdw_fzr) ;
        bfdw_lxdh=(KeyEditText)parentView.findViewById(R.id.bfdw_lxdh) ;
        bfdw_lxr=(KeyEditText)parentView.findViewById(R.id.bfdw_lxr) ;
    }

    /**
     * 第一书记筛选模块
     */
    public void initDYSJ(){
        dysj_name=(KeyEditText)parentView.findViewById(R.id.dysj_name) ;
        dysj_phone=(KeyEditText)parentView.findViewById(R.id.dysj_phone) ;
    }
    /**
     * 工作队筛选模块
     */
    public void initGZD(){
        dysj_name=(KeyEditText)parentView.findViewById(R.id.dysj_name) ;
        dysj_phone=(KeyEditText)parentView.findViewById(R.id.dysj_phone) ;
    }
    /**
     * 责任组筛选模块
     */
    public void initZRZ(){
        dysj_name=(KeyEditText)parentView.findViewById(R.id.dysj_name) ;
        dysj_phone=(KeyEditText)parentView.findViewById(R.id.dysj_phone) ;
    }


    @SuppressLint("CheckResult")
    public void loadData(){

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
