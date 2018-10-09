package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Adapter.PovertyBaseInfoAdapter;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.StatisticsDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.BaseInfo;
import com.dyhl.dusky.huangchuanfp.Module.entity.PoertyType;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyDetail;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyInformation;
import com.dyhl.dusky.huangchuanfp.Module.entity.Reson;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.Chart.MyAxisValueFormatter;
import com.dyhl.dusky.huangchuanfp.Utils.Chart.MyHAxisValueFormatter;
import com.dyhl.dusky.huangchuanfp.Utils.Chart.MyYearAxisValueFormatter;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.MyComparator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class StatisticsDetailReasonFragment extends BaseFragment implements OnChartValueSelectedListener {
    ArrayList<Reson> types;
    ArrayList<BarEntry> entries;
    @BindView(R.id.HorizontalBarChart)
    HorizontalBarChart mChart;

    @BindView(R.id.tip)
    TextView tip;

    public static StatisticsDetailReasonFragment newInstance() {
        StatisticsDetailReasonFragment fragment=  new StatisticsDetailReasonFragment();
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_statistics_reason;
    }


    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        //loadData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event){
        if(!TextUtils.isEmpty(event)) {
            switch (event) {
                case "StatisticsDetailReasonFragment":
                    loadData();
                    break;

            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void initHorizontalBarChart() {

       // mChart.setOnChartValueSelectedListener(this);
        // mChart.setHighlightEnabled(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);
//设置是否可以缩放 x和y，默认true
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        MyHAxisValueFormatter myHAxisValueFormatter = new MyHAxisValueFormatter();
        myHAxisValueFormatter.setTypes(types);
        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setLabelCount(types.size());
        xl.setValueFormatter(myHAxisValueFormatter);
        xl.setGranularity(10f);

        MyAxisValueFormatter myAxisValueFormatter = new MyAxisValueFormatter();
        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setValueFormatter(myAxisValueFormatter);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setValueFormatter(myAxisValueFormatter);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        mChart.setFitBars(true);
        //mChart.animateY(2500);


    }

    private void setData() {
        initHorizontalBarChart();
        float barWidth = 12f;


        BarDataSet set1;

        set1 = new BarDataSet(entries, "致贫原因分布");
        set1.setDrawIcons(false);
        set1.setColor(getResources().getColor(R.color.colorPrimary));
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(barWidth);
        mChart.setData(data);
        mChart.invalidate();
    }


    float largest=-1;
    @SuppressLint("CheckResult")
    public void loadData(){
        largest=-1;
        types=new ArrayList<>();
        entries = new ArrayList<BarEntry>();
        RetrofitHelper.getStatisticsAPI()
                .getpovertyReson(StatisticsDetailActivity.permissionstag)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            JSONArray jsonArray = new JSONArray(apiMsg.getResult());
                            int size=jsonArray.length();
                            //转成list
                            List<JSONObject> list = new ArrayList<JSONObject> ();
                            JSONObject jsonObj = null;
                            for (int i = 0; i < size; i++) {
                                jsonObj = (JSONObject)jsonArray.get(i);
                                list.add(jsonObj);
                            }
                            Collections.sort(list,new MyComparator());

                            for (int i = 0; i < size; i++) {
                                Reson type=JSON.parseObject(list.get(i).toString(),Reson.class);
                                if(type!=null){
                                    types.add(type);
                                    float num=Float.parseFloat(type.getNum());
                                    if(largest<num){
                                        largest=num;
                                    }
                                    entries.add(new BarEntry(i*20,Float.parseFloat(type.getNum())));//type.getReson()+"("+type.getNum()+")")
                                }
                            }

                            if(entries.size()>0){
                                setData();
                                tip.setHint("");
                                mChart.setVisibility(View.VISIBLE);
                            }else{
                                tip.setHint("没有查询到相关信息");
                                mChart.setVisibility(View.GONE);
                            }
                            break;
                        case "-1":
                        case "-2":
                            tip.setHint("没有查询到相关信息");
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            mChart.setVisibility(View.GONE);
                            break;
                    }

                }, throwable -> {

                    tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                    mChart.setVisibility(View.GONE);
                });
    }




    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
