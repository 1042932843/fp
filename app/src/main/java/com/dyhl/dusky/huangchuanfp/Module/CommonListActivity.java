package com.dyhl.dusky.huangchuanfp.Module;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.BFDWListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.GZDAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.GzdInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.LiableListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.MajorSecretaryAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PkcListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.ProblemAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.ResponsibilityGroupAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SignListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SignListInMain2Adapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SpinnerAdapter_lv3;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Design.MySpinner;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.ChoiceFragment;
import com.dyhl.dusky.huangchuanfp.Module.Fragment.CommonListTypeDrawerFragment;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BFDWInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.CommonData;
import com.dyhl.dusky.huangchuanfp.Module.entity.GzdInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Module.entity.MajorSecretary;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PoertyType;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Module.entity.Problem;
import com.dyhl.dusky.huangchuanfp.Module.entity.ResponsibilityGroup;
import com.dyhl.dusky.huangchuanfp.Module.entity.SignInList;
import com.dyhl.dusky.huangchuanfp.Module.entity.Town;
import com.dyhl.dusky.huangchuanfp.Module.entity.Village;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommonListActivity extends BaseActivity {

    protected ProgressDialog dialog;
    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @BindView(R.id.totallayout)
    RelativeLayout totallayout;
    @BindView(R.id.container)
    FrameLayout container;

    @BindView(R.id.ad_lv1)
    MySpinner ad_lv1;

    @BindView(R.id.ad_lv2)
    AppCompatSpinner ad_lv2;

    @BindView(R.id.ad_lv3)
    AppCompatSpinner ad_lv3;

    SpinnerAdapter spinnerAdapter,spinnerAdapter2;
    SpinnerAdapter_lv3 spinnerAdapterlv3;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    @BindView(R.id.img_right)
    ImageView img_right;
    @OnClick(R.id.img_right)
    public void saixuan(){
        if(drawer.isDrawerOpen(Gravity.END)){
            drawer.closeDrawer(Gravity.END);
        }else{
            drawer.openDrawer(Gravity.END);
        }
    }

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;

    @BindView(R.id.tip)
    TextView tip;
    //帮扶单位信息类型
    private List<BFDWInfo> bfdw_datas;
    private BFDWListAdapter bfdw_adapter;

    //贫困村信息类型
    private List<PkcInfo> pkc_datas;
    private PkcListAdapter pkc_adapter;

    //贫困户信息类型
    private List<PovertyInformation> pkh_datas;
    private PovertyListAdapter pkh_adapter;

    //日志类型
    private List<SignInList> rz_datas;
    private SignListInMain2Adapter rz_adapter;

    //帮扶责任人类型
    private List<Liable> zrr_datas;
    private LiableListAdapter zrr_adapter;

    //上传问题类型
    private List<Problem> wtsb_datas;
    private ProblemAdapter wtsb_adapter;

    //第一书记列表
    private List<MajorSecretary> MajorSecretary_datas;
    private MajorSecretaryAdapter MajorSecretary_adapter;
    //驻村工作队
    private List<GzdInfo> GzdInfo_datas;
    private GZDAdapter GzdInfo_adapter;
    //攻坚责任组
    private List<ResponsibilityGroup> ResponsibilityGroup_datas;
    private ResponsibilityGroupAdapter ResponsibilityGroup_adapter;

    private boolean mIsRefreshing = false;
    int currentPage=1;
    int pageSize=20;

    String type;
    int type_position;

    @BindView(R.id.total)
    TextView totalTv;
    String total;
    @Override
    public int getLayoutId() {
        return R.layout.activity_commonlist;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        type_position =getIntent().getIntExtra("position",-1);
        code=getIntent().getStringExtra("TownCode");
        towidtag=getIntent().getStringExtra("TownCode");
        if(code==null||code.equals("")){
            code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        }
        CommonListTypeDrawerFragment commonListTypeDrawerFragment=CommonListTypeDrawerFragment.newInstance(type_position);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, commonListTypeDrawerFragment);
        transaction.commit();

        switch (type_position){
            case R.id.main2_pkc:
                type="贫困村";
                break;
            case R.id.main2_pkh:
                type="贫困户";
                break;
            case R.id.main2_bfzrr:
                type="帮扶责任人";
                break;
            case R.id.main2_bfrz:
                type="帮扶日志";
                break;
            case R.id.main2_sbwt:
                type="上报问题";
                break;
            case R.id.main2_bfdw:
                type="帮扶单位";
                //totallayout.setVisibility(View.GONE);
                break;

            case R.id.main2_dysj:
                type="第一书记";
                break;

            case R.id.main2_gzd:
                type="驻村工作队";
                break;

            case R.id.main2_zrz:
                type="责任组";
                break;
                default:

                    break;

        }
        initWidget();
        initAllTown();
    }
    ArrayList<Town> towns;
    ArrayList<Town> all;
    ArrayList<Village> villages;
    int a_p=0;
    int t_p=0;
    int v_p=0;
    String towidtag;
    String permissionstag;
    boolean x=true;
    @SuppressLint("CheckResult")
    private void initAllTown() {
       String permissions= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        towns =new ArrayList<>();
        all=new ArrayList<>();

        Town townall=new Town();
        if(TextUtils.isEmpty(towidtag)){
            towidtag= PreferenceUtil.getStringPRIVATE("townid", UserState.NA);
        }
        if(TextUtils.isEmpty(permissionstag)){
            permissionstag= permissions;
        }
        if(!TextUtils.isEmpty(getIntent().getStringExtra("code"))&&x){
            towidtag=getIntent().getStringExtra("code");
            x=false;
        }

        RetrofitHelper.getCommonListAPI()
                .getArea(PreferenceUtil.getStringPRIVATE("permissions", ""))
                .compose(this.bindToLifecycle())
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
                                        if(i>1){
                                            t_p=i+1;//+1是因为白板项
                                        }else{
                                            t_p=i;
                                        }

                                    }
                                    towns.add(town);
                                    ArrayList<Village> vi=town.getVillages();

                                    for(int j=0;j<vi.size();j++){
                                        if(x){//如果是true才执行里面的ui选择。
                                        if(permissionstag.equals(vi.get(j).getCode())){
                                            if(j>1){
                                                v_p=j+1;
                                            }else{
                                                v_p=j;
                                            }

                                        }
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

                            townall.setName(jsonObject.getString("name"));
                            all.add(townall);
                            spinnerAdapter2=new SpinnerAdapter(all,this);
                            ad_lv1.setAdapter(spinnerAdapter2);
                            ad_lv1.setOnItemSelectedListener(onItemSelectedListener0);
                            ad_lv2.setOnItemSelectedListener(onItemSelectedListener);
                            ad_lv3.setOnItemSelectedListener(onItemSelectedListener2);
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
    private AdapterView.OnItemSelectedListener onItemSelectedListener0= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spinnerAdapter=new SpinnerAdapter(towns,CommonListActivity.this);
            ad_lv2.setAdapter(spinnerAdapter);

            if(position==a_p){
                a_p=-1;
                if(spinnerAdapter.getCount()>t_p){
                    ad_lv2.setSelection(t_p);
                }

            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };


    private AdapterView.OnItemSelectedListener onItemSelectedListener= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //选择列表项的操作
            villages=towns.get(position).getVillages();
            towidtag=towns.get(position).getCode();
            spinnerAdapterlv3=new SpinnerAdapter_lv3(villages,CommonListActivity.this);
            ad_lv3.setAdapter(spinnerAdapterlv3);

            if(position==t_p){
                if(spinnerAdapterlv3.getCount()>v_p){
                    ad_lv3.setSelection(v_p);
                }
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
            code= villages.get(position).getCode();
            permissionstag=villages.get(position).getCode();
            if(isFirst){
                isFirst=false;
            }else{
                currentPage=1;
                if(rz_datas!=null){
                    rz_datas.clear();
                    rz_adapter.notifyDataSetChanged();
                }
                if(zrr_datas!=null){
                    zrr_datas.clear();
                    zrr_adapter.notifyDataSetChanged();
                }
                if(pkh_datas!=null){
                    pkh_datas.clear();
                    pkh_adapter.notifyDataSetChanged();
                }
                if(wtsb_datas!=null){
                    wtsb_datas.clear();
                    wtsb_adapter.notifyDataSetChanged();
                }
                if(pkc_datas!=null){
                    pkc_datas.clear();
                    pkc_adapter.notifyDataSetChanged();
                }
                if(bfdw_datas!=null){
                    bfdw_datas.clear();
                    bfdw_adapter.notifyDataSetChanged();
                }
                if(MajorSecretary_datas!=null){
                    MajorSecretary_datas.clear();
                    MajorSecretary_adapter.notifyDataSetChanged();
                }
                if(GzdInfo_datas!=null){
                    GzdInfo_datas.clear();
                    GzdInfo_adapter.notifyDataSetChanged();
                }
                if(ResponsibilityGroup_datas!=null){
                    ResponsibilityGroup_datas.clear();
                    ResponsibilityGroup_adapter.notifyDataSetChanged();
                }
                totalTv.setText("");
                loadData();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };

    @Override
    public void initToolBar() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public static String code="";
    String id="",publics="",reply="",departmentname="",incharge="",contact="",name="",responsible="",pvertyattribute="",overcomeattribute="",familyid="",position="",reson="",phone="",start="",end="",town="",village="",outpoverty="";
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CommonData event){
        if(event!=null){
            departmentname=event.getDepartmentname();
            incharge=event.getIncharge();
            contact=event.getContact();
            name=event.getName();
            responsible=event.getResponsible();
            pvertyattribute=event.getPvertyattribute();
            overcomeattribute=event.getOverpvertyattribute();
            position=event.getPosition();
            publics=event.getPublics();
            reply=event.getReply();
            reson=event.getReson();
            phone=event.getPhone();
            start=event.getStart();
            end=event.getEnd();
            village=event.getVillage();
            outpoverty=event.getOutpoverty();
            if(TextUtils.isEmpty(publics)&&TextUtils.isEmpty(reply)&&TextUtils.isEmpty(contact)&&TextUtils.isEmpty(incharge)&&TextUtils.isEmpty(departmentname)&&TextUtils.isEmpty(town)&&TextUtils.isEmpty(village)&&TextUtils.isEmpty(outpoverty)&&TextUtils.isEmpty(name)&&TextUtils.isEmpty(responsible)&&TextUtils.isEmpty(pvertyattribute)&&TextUtils.isEmpty(position)&&TextUtils.isEmpty(reson)&&TextUtils.isEmpty(phone)&&TextUtils.isEmpty(start)&&TextUtils.isEmpty(end)){
                event.setLight(false);
            }else{
                event.setLight(true);
            }
            img_right.setImageResource(event.isLight()?R.drawable.main2_saixuan_light:R.drawable.main2_saixuan);
            currentPage=1;
            if(rz_datas!=null){
                rz_datas.clear();
                rz_adapter.notifyDataSetChanged();
            }
            if(zrr_datas!=null){
                zrr_datas.clear();
                zrr_adapter.notifyDataSetChanged();
            }
            if(pkh_datas!=null){
                pkh_datas.clear();
                pkh_adapter.notifyDataSetChanged();
            }
            if(wtsb_datas!=null){
                wtsb_datas.clear();
                wtsb_adapter.notifyDataSetChanged();
            }
            if(pkc_datas!=null){
                pkc_datas.clear();
                pkc_adapter.notifyDataSetChanged();
            }
            if(bfdw_datas!=null){
                bfdw_datas.clear();
                bfdw_adapter.notifyDataSetChanged();
            }
            if(MajorSecretary_datas!=null){
                MajorSecretary_datas.clear();
                MajorSecretary_adapter.notifyDataSetChanged();
            }
            if(GzdInfo_datas!=null){
                GzdInfo_datas.clear();
                GzdInfo_adapter.notifyDataSetChanged();
            }
            if(ResponsibilityGroup_datas!=null){
                ResponsibilityGroup_datas.clear();
                ResponsibilityGroup_adapter.notifyDataSetChanged();
            }

            if(drawer.isDrawerOpen(Gravity.END)){
                drawer.closeDrawer(Gravity.END);
            }else{
                drawer.openDrawer(Gravity.END);
            }
            totalTv.setText("");
            loadData();
        }

    }

    @SuppressLint("CheckResult")
    public void loadData() {
        total="";
        mIsRefreshing=true;
        String id=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);
       if(code==null||code.equals("")){
           code= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
       }

        switch (type_position){
            case R.id.main2_pkc:
                type="贫困村";
                getPKCList(code);
                break;
            case R.id.main2_pkh:
                type="贫困户";
                getPKHList(code,name,responsible,pvertyattribute,overcomeattribute,reson);
                break;
            case R.id.main2_bfzrr:
                type="帮扶责任人";
                getZZRList(code,responsible,familyid,position,phone);
                break;
            case R.id.main2_bfrz:
                type="帮扶日志";
                getBFRZList(publics,code,name,responsible,start,end);
                break;
            case R.id.main2_sbwt:
                type="上报问题";
                getSBWTList(code,name,responsible,start,end,publics,reply);
                break;
            case R.id.main2_bfdw:
                type="帮扶单位";
                getBFDWList(id,code,departmentname,incharge,contact,phone);
                break;
            case R.id.main2_dysj:
                type="第一书记";
                getDYSJList(code,phone,name);
                break;
            case R.id.main2_gzd:
                type="驻村工作队";
                getGZDList(code,phone,name);
                break;
            case R.id.main2_zrz:
                type="责任组";
                getZRZList(code,phone,name);
                break;

            default:

                break;

        }



    }

    @SuppressLint("CheckResult")
    public void getPKCList(String code){
        RetrofitHelper.getCommonListAPI()
                .getPKCList(code,town,village,outpoverty,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"个，"+"已脱贫"+obj.getString("totaloutofpoverty")+"个";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                PkcInfo povertyInformation= JSON.parseObject(o, PkcInfo.class);
                                pkc_datas.add(povertyInformation);
                            }
                            if(pkc_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                            }
                            pkc_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                            currentPage += 1;

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @SuppressLint("CheckResult")
    public void getPKHList(String code,String name,String responsible,String povertyattribute,String overcomeattribute,String reson){
        RetrofitHelper.getCommonListAPI()
                .getPKHList(code,name,responsible,povertyattribute,overcomeattribute,reson,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try {
                                total = "共" + obj.getString("population") + "人，" + "已脱贫" + obj.getString("overpovertypopulation") + "人";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                PovertyInformation povertyInformation= JSON.parseObject(o, PovertyInformation.class);
                                pkh_datas.add(povertyInformation);
                            }
                            if(pkh_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                                pkh_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                                currentPage += 1;
                            }
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @SuppressLint("CheckResult")
    public void getZZRList(String code,String name,String familyid,String position,String phone){
        RetrofitHelper.getCommonListAPI()
                .getZZRList(code,name,position,phone,familyid,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"人，"+"帮扶"+obj.getString("totalbfpkh")+"户";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }

                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                Liable liable= JSON.parseObject(o, Liable.class);
                                zrr_datas.add(liable);
                            }
                            if(zrr_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                                zrr_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                                currentPage += 1;
                            }

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @SuppressLint("CheckResult")
    public void getBFRZList(String publics,String code,String name,String responsible,String start,String end){
        RetrofitHelper.getCommonListAPI()
                .getBFRZList(publics,code,name,responsible,start,end,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"条日志，"+"涉及"+obj.getString("totalresponsible")+"个帮扶人";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }

                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                SignInList signInList= JSON.parseObject(o, SignInList.class);
                                rz_datas.add(signInList);
                            }
                            if(rz_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                                rz_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                                currentPage ++;
                            }

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @SuppressLint("CheckResult")
    public void getSBWTList(String code,String name,String responsible,String start,String end,String publics,String reply){
        RetrofitHelper.getCommonListAPI()
                .getSBWTList(code,name,responsible,start,end,publics,reply,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"条问题，"+"涉及"+obj.getString("totalpovertyfamily")+"个贫困户";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }

                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                Problem problem= JSON.parseObject(o, Problem.class);
                                wtsb_datas.add(problem);
                            }

                            if(wtsb_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                                wtsb_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                                currentPage += 1;
                            }
                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @SuppressLint("CheckResult")
    public void getBFDWList(String id,String code,String departmentname,String incharge,String contact,String phone){
        RetrofitHelper.getCommonListAPI()
                .getBFDWList(id,code,departmentname,incharge,contact,phone,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"个帮扶单位";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                BFDWInfo povertyInformation= JSON.parseObject(o, BFDWInfo.class);
                                bfdw_datas.add(povertyInformation);
                            }
                            if(bfdw_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                            }
                            bfdw_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                            currentPage += 1;

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }


    @SuppressLint("CheckResult")
    public void getDYSJList(String code,String phone,String name){
        RetrofitHelper.getCommonListAPI()
                .getDYSJList(code,phone,name,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"个第一书记";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                MajorSecretary  povertyInformation= JSON.parseObject(o, MajorSecretary.class);
                                MajorSecretary_datas.add(povertyInformation);
                            }
                            if(MajorSecretary_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                            }
                            MajorSecretary_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                            currentPage += 1;

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @SuppressLint("CheckResult")
    public void getZRZList(String code,String phone,String name){
        RetrofitHelper.getCommonListAPI()
                .getZRZList(code,phone,name,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"个责任组成员";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                ResponsibilityGroup  povertyInformation= JSON.parseObject(o, ResponsibilityGroup.class);
                                ResponsibilityGroup_datas.add(povertyInformation);
                            }
                            if(ResponsibilityGroup_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                            }
                            ResponsibilityGroup_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                            currentPage += 1;

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }
    @SuppressLint("CheckResult")
    public void getGZDList(String code,String phone,String name){
        RetrofitHelper.getCommonListAPI()
                .getGZDList(code,phone,name,currentPage,pageSize)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            try{
                                total="共"+obj.getString("totalCount")+"个驻村工作队成员";
                                totalTv.setText(total);
                            }catch (Exception e){

                            }
                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                GzdInfo  povertyInformation= JSON.parseObject(o, GzdInfo.class);
                                GzdInfo_datas.add(povertyInformation);
                            }
                            if(GzdInfo_datas.size()<=0){
                                tip.setVisibility(View.VISIBLE);
                            }else {
                                tip.setVisibility(View.GONE);
                            }
                            GzdInfo_adapter.notifyItemRangeChanged(((currentPage-1)*pageSize),pageSize);
                            currentPage += 1;

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }, throwable -> {
                    dialog.dismiss();
                    mIsRefreshing = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    LinearLayoutManager layoutManager;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    public void initRecyclerView(){
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        switch (type_position){
            case R.id.main2_zrz:
                //type="责任组";
                ResponsibilityGroup_datas=new ArrayList<>();
                ResponsibilityGroup_adapter=new ResponsibilityGroupAdapter(ResponsibilityGroup_datas,this);
                recyclerView.setAdapter(ResponsibilityGroup_adapter);

                break;
            case R.id.main2_gzd:
                //type="工作队";
                GzdInfo_datas=new ArrayList<>();
                GzdInfo_adapter=new GZDAdapter(GzdInfo_datas,this);
                recyclerView.setAdapter(GzdInfo_adapter);

                break;
            case R.id.main2_dysj:
                //type="第一书记";
                MajorSecretary_datas=new ArrayList<>();
                MajorSecretary_adapter=new MajorSecretaryAdapter(MajorSecretary_datas,this);
                recyclerView.setAdapter(MajorSecretary_adapter);

                break;

            case R.id.main2_pkc:
                //type="贫困村";
                pkc_datas=new ArrayList<>();
                pkc_adapter=new PkcListAdapter(pkc_datas,this);
                recyclerView.setAdapter(pkc_adapter);
                pkc_adapter.setOnItemClickListener(new PkcListAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent it=new Intent(CommonListActivity.this, PkcDetailActivity.class);
                        it.putExtra("PkcInfo",pkc_datas.get(position));
                        startActivity(it);
                    }

                    @Override
                    public void onLongClick(int position) {

                    }
                });
                break;
            case R.id.main2_pkh:
                //type="贫困户";
                pkh_datas=new ArrayList<>();
                pkh_adapter=new PovertyListAdapter(pkh_datas,this);
                recyclerView.setAdapter(pkh_adapter);
                pkh_adapter.setOnItemClickListener(new PovertyListAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent it=new Intent(CommonListActivity.this, PovertyDetailActivity.class);
                        it.putExtra("PovertyInfo",pkh_datas.get(position));
                        startActivity(it);
                    }
                    @Override
                    public void onLongClick(int position) {
                        //ToastUtil.ShortToast(position+"");
                    }
                });
                break;
            case R.id.main2_bfzrr:
                //type="帮扶责任人";
                zrr_datas=new ArrayList<>();
                zrr_adapter=new LiableListAdapter(zrr_datas,this);
                recyclerView.setAdapter(zrr_adapter);
                zrr_adapter.setOnItemClickListener(new LiableListAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent it=new Intent(CommonListActivity.this,LiableDetailActivity.class);
                        it.putExtra("LiableInfo",zrr_datas.get(position));
                        startActivity(it);
                    }
                    @Override
                    public void onLongClick(int position) {
                        //ToastUtil.ShortToast(position+"");
                    }
                });
                break;
            case R.id.main2_bfrz:
                //type="帮扶日志";
                rz_datas=new ArrayList<>();
                rz_adapter=new SignListInMain2Adapter(rz_datas,this);
                recyclerView.setAdapter(rz_adapter);
                rz_adapter.setOnItemClickListener(new SignListInMain2Adapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        //ToastUtil.showShort(CommonListActivity.this,position+"");
                        Intent it=new Intent(CommonListActivity.this,LogDetailActivity.class);
                        it.putExtra("num",rz_datas.get(position).getNum());
                        startActivity(it);
                    }
                    @Override
                    public void onLongClick(int position) {
                        //ToastUtil.ShortToast(position+"");
                    }
                });
                break;
            case R.id.main2_sbwt:
                //type="上报问题";
                wtsb_datas=new ArrayList<>();
                wtsb_adapter=new ProblemAdapter(wtsb_datas,this);
                recyclerView.setAdapter(wtsb_adapter);
                wtsb_adapter.setOnItemClickListener(new ProblemAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent it=new Intent(CommonListActivity.this,PoertyProblemDetailActivity.class);
                        it.putExtra("num",wtsb_datas.get(position).getNum());
                        startActivity(it);
                    }
                    @Override
                    public void onLongClick(int position) {
                        //ToastUtil.ShortToast(position+"");
                    }
                });
                break;

            case R.id.main2_bfdw:
                //type="帮扶单位";
                bfdw_datas=new ArrayList<>();
                bfdw_adapter=new BFDWListAdapter(bfdw_datas,this);
                recyclerView.setAdapter(bfdw_adapter);
                bfdw_adapter.setOnItemClickListener(new BFDWListAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent it=new Intent(CommonListActivity.this,DwDetailActivity.class);
                        it.putExtra("item",bfdw_datas.get(position));
                        startActivity(it);
                    }
                    @Override
                    public void onLongClick(int position) {
                        //ToastUtil.ShortToast(position+"");
                    }
                });
                break;
            default:

                break;
        }
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData();
            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();

    }


    @SuppressLint("ClickableViewAccessibility")
    private void setRecycleNoScroll() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefreshing;
            }
        });
    }


    private void initWidget() {
        dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        dialog.show();
        setAppTitle(type);
        img_right.setVisibility(View.VISIBLE);
        img_right.setImageResource(R.drawable.main2_saixuan);
        initRecyclerView();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            currentPage=1;
            totalTv.setText("");
            if(rz_datas!=null){
                rz_datas.clear();
                rz_adapter.notifyDataSetChanged();
            }
            if(zrr_datas!=null){
                zrr_datas.clear();
                zrr_adapter.notifyDataSetChanged();
            }
            if(pkh_datas!=null){
                pkh_datas.clear();
                pkh_adapter.notifyDataSetChanged();
            }
            if(wtsb_datas!=null){
                wtsb_datas.clear();
                wtsb_adapter.notifyDataSetChanged();
            }

            if(pkc_datas!=null){
                pkc_datas.clear();
                pkc_adapter.notifyDataSetChanged();
            }
            if(bfdw_datas!=null){
                bfdw_datas.clear();
                bfdw_adapter.notifyDataSetChanged();
            }
            if(MajorSecretary_datas!=null){
                MajorSecretary_datas.clear();
                MajorSecretary_adapter.notifyDataSetChanged();
            }

            if(GzdInfo_datas!=null){
                GzdInfo_datas.clear();
                GzdInfo_adapter.notifyDataSetChanged();
            }

            if(ResponsibilityGroup_datas!=null){
                ResponsibilityGroup_datas.clear();
                ResponsibilityGroup_adapter.notifyDataSetChanged();
            }
            mEndlessRecyclerOnScrollListener.refresh();
            //initAllTown();
            loadData();
        });
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
