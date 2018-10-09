package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SignListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.LogDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.LoglistActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.Liable;
import com.dyhl.dusky.huangchuanfp.Module.entity.SignInList;
import com.dyhl.dusky.huangchuanfp.Net.ApiConstants;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.PreferenceUtil;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LiableLoglistFragment extends BaseFragment {
    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @BindView(R.id.tip)
            TextView tip;

    String type;
    Liable liable;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private List<SignInList> datas;
    private SignListAdapter adapter;
    private boolean mIsRefreshing = false;
    int currentPage=1;

    public static LiableLoglistFragment newInstance(String type, Liable liable) {
        LiableLoglistFragment fragment=  new LiableLoglistFragment();
        fragment.type=type;
        fragment.liable=liable;
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_liable_loglist;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initRecyclerView();
        loadData();
    }


    public void loadData() {
        mIsRefreshing=true;
        String url = ApiConstants.Base_URL + "selectWorkPicture";
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("key",liable.getResponsibleid());
        multipartBodyBuilder.addFormDataPart("currentPage",currentPage+"");
        multipartBodyBuilder.addFormDataPart("pageSize", 20+"");

        RequestBody requestBody = multipartBodyBuilder.build();
        Request.Builder RequestBuilder = new Request.Builder();
        RequestBuilder.url(url);// 添加URL地址
        RequestBuilder.post(requestBody);
        Request request = RequestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("err", "result:" + e);
                call.cancel();
                mIsRefreshing=false;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.d("reg", "result:" + str);
                mIsRefreshing=false;
                try {
                    JSONObject obj = new JSONObject(str);
                    JSONObject data= obj.getJSONObject("result");
                    JSONArray jsonArray = data.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String o = jsonArray.getString(i);
//                        RedFlag redFlag = JSON.parseObject(o, RedFlag.class);
                        SignInList signInList= JSON.parseObject(o, SignInList.class);
                        datas.add(signInList);
                    }

                    currentPage++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(datas.size()>0){
                            tip.setText("");
                            adapter.notifyDataSetChanged();
                        }else{
                            tip.setText("没有查询到相关信息");
                        }

                    }
                });
                call.cancel();
            }
        });

    }

    public void initRecyclerView(){
        datas=new ArrayList<>();
        //去掉recyclerView动画处理闪屏
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SignListAdapter(datas,getActivity());
        recyclerView.setAdapter(adapter);
        mEndlessRecyclerOnScrollListener =new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadData();
            }
        };
        recyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
        setRecycleNoScroll();
        adapter.setOnItemClickListener(new SignListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent it=new Intent(getActivity(),LogDetailActivity.class);
                it.putExtra("num",datas.get(position).getNum());
                startActivity(it);
            }
            @Override
            public void onLongClick(int position) {

            }
        });
    }

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
