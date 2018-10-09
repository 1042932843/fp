package com.dyhl.dusky.huangchuanfp.Module;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dyhl.dusky.huangchuanfp.Adapter.AnnuncementAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.SignListAdapter;
import com.dyhl.dusky.huangchuanfp.Adapter.helper.EndlessRecyclerOnScrollListener;
import com.dyhl.dusky.huangchuanfp.Base.BaseActivity;
import com.dyhl.dusky.huangchuanfp.Base.UserState;
import com.dyhl.dusky.huangchuanfp.Module.entity.Annuncement;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoglistActivity extends BaseActivity {

    @BindView(R.id.play_list)
    RecyclerView recyclerView;

    @OnClick(R.id.img_back)
    public void back(){
        finish();
    }

    @BindView(R.id.txt_title)
    TextView apptitle;
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private List<SignInList> datas;
    private SignListAdapter adapter;
    private boolean mIsRefreshing = false;
    int currentPage=1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_loglist;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initWidget();
    }

    @Override
    public void initToolBar() {

    }
    @Override
    public void loadData() {
        mIsRefreshing=true;
            String url = ApiConstants.Base_URL + "selectWorkPicture";
            OkHttpClient okHttpClient = new OkHttpClient();
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
            multipartBodyBuilder.setType(MultipartBody.FORM);
            String id=  PreferenceUtil.getStringPRIVATE("id", UserState.NA);
            multipartBodyBuilder.addFormDataPart("key",id);
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
                    mIsRefreshing=false;
                    Log.d("reg", "result:" + str);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new SignListAdapter(datas,this);
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
                //ToastUtil.showShort(LoglistActivity.this,position+"");
                Intent it=new Intent(LoglistActivity.this,LogDetailActivity.class);
                it.putExtra("num",datas.get(position).getNum());
                startActivity(it);
            }
            @Override
            public void onLongClick(int position) {
                //ToastUtil.ShortToast(position+"");
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


    private void initWidget() {
        setAppTitle("我的日志");
        initRecyclerView();
    }

    public void setAppTitle(String title) {
        Log.d("reg", "title:" + title);
        apptitle.setText(title);
    }



}
