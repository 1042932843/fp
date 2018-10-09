package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.ChoiceAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.LiableListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.CommonListActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyDetail;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PovertyDetailLiableInfoFragment extends BaseFragment {
    private String type;
    String idcard;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tip)
    TextView tip;
    protected ProgressDialog dialog;
    int currentPage=1;
    int pageSize=10;

    //帮扶责任人类型
    private List<Liable> datas;
    private LiableListAdapter zrr_adapter;

    public static PovertyDetailLiableInfoFragment newInstance(String type,String idcard) {
        PovertyDetailLiableInfoFragment fragment=  new PovertyDetailLiableInfoFragment();
        fragment.type=type;
        fragment.idcard=idcard;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_poverty_liableinfo;
    }


    @Override
    public void finishCreateView(Bundle state) {
        initRec();
        loadData();
    }
    private boolean mIsRefreshing = false;
    LinearLayoutManager layoutManager;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    public void initRec(){
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        datas=new ArrayList<>();
        zrr_adapter=new LiableListAdapter(datas,getActivity());
        recyclerView.setAdapter(zrr_adapter);
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

    @SuppressLint("CheckResult")
    public void loadData() {
        mIsRefreshing=true;
        dialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        getZZRList(CommonListActivity.code,"",idcard,"","");
    }

    @SuppressLint("CheckResult")
    public void getZZRList (String code, String name, String familyid, String position, String
            phone){
        RetrofitHelper.getCommonListAPI()
                .getZZRList(code, name, position, phone, familyid, currentPage, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    mIsRefreshing=false;
                    String a = bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a, ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state) {
                        case "0":
                            //ToastUtil.ShortToast(apiMsg.getMessage());
                            JSONObject obj = new JSONObject(apiMsg.getResult());
                            /*try {
                                total = "共" + obj.getString("totalCount") + "人，" + "帮扶" + obj.getString("totalbfpkh") + "户";
                                totalTv.setText(total);
                            } catch (Exception e) {

                            }*/

                            final JSONArray jsonArray = obj.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String o = jsonArray.getString(i);
                                Liable liable = JSON.parseObject(o, Liable.class);
                                datas.add(liable);
                            }
                            if (datas.size() <= 0) {
                                tip.setVisibility(View.VISIBLE);
                            } else {
                                tip.setVisibility(View.GONE);
                                zrr_adapter.notifyItemRangeChanged(((currentPage - 1) * pageSize), pageSize);
                                currentPage += 1;
                            }

                            break;
                        case "-1":
                        case "-2":
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;

                    }

                }, throwable -> {
                    mIsRefreshing=false;
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
