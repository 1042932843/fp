package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.LiableListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PkcListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyFamilyInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.CommonListActivity;
import com.dyhl.dusky.huangchuanfp.Module.LiableDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.PkcDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BFDWInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Module.entity.PkcInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DwPkcFragment extends BaseFragment {
    private BFDWInfo bfdwInfo;

    List<PovertyInformation> datas;
    int currentPage=1;
    int pageSize=10;
    String permissions;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tip)
    TextView tip;

    @BindView(R.id.total)
    TextView totalTv;
    String total;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    PovertyFamilyInfoAdapter adapter;
    protected ProgressDialog dialog;

    //贫困村信息类型
    private List<PkcInfo> pkc_datas;
    private PkcListAdapter pkc_adapter;

    public static DwPkcFragment newInstance(BFDWInfo bfdwInfo) {
        DwPkcFragment fragment=  new DwPkcFragment();
        fragment.bfdwInfo=bfdwInfo;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_dwpkc;
    }

    private boolean mIsRefreshing = false;
    @Override
    public void finishCreateView(Bundle state) {
        initRecyclerView();
        dialog = new ProgressDialog(getContext(), ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setMessage("请求中...");
        dialog.show();
        permissions= PreferenceUtil.getStringPRIVATE("permissions", UserState.NA);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mIsRefreshing = true;
            currentPage=1;

            if(pkc_datas!=null){
                pkc_datas.clear();
                pkc_adapter.notifyDataSetChanged();
            }
            mEndlessRecyclerOnScrollListener.refresh();
            //initAllTown();
            getPKCList();
        });

        getPKCList();
    }


    LinearLayoutManager layoutManager;
    EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    public void initRecyclerView(){
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //type="贫困村";
        pkc_datas=new ArrayList<>();
        pkc_adapter=new PkcListAdapter(pkc_datas,getContext());
        recyclerView.setAdapter(pkc_adapter);
        pkc_adapter.setOnItemClickListener(new PkcListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent it=new Intent(getActivity(), PkcDetailActivity.class);
                it.putExtra("PkcInfo",pkc_datas.get(position));
                startActivity(it);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getPKCList();
            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();

    }

    @SuppressLint("CheckResult")
    public void getPKCList(){
        RetrofitHelper.getCommonListAPI()
                .getPKCList(permissions,bfdwInfo.getDepartmentid(),currentPage,pageSize)
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


    @SuppressLint("ClickableViewAccessibility")
    private void setRecycleNoScroll() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mIsRefreshing;
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
}
