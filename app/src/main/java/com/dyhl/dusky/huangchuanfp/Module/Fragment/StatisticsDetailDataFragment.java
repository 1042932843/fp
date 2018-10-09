package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.StatisticsDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.OutPovertyData;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.Chart.DayAxisValueFormatter;
import com.dyhl.dusky.huangchuanfp.Utils.Chart.MyAxisValueFormatter;
import com.dyhl.dusky.huangchuanfp.Utils.Chart.MyYearAxisValueFormatter;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class StatisticsDetailDataFragment extends BaseFragment implements OnChartValueSelectedListener {
    ArrayList<OutPovertyData> datas;
    private List<BarEntry> entries;
    @BindView(R.id.BarChart)
    BarChart mChart;

    @BindView(R.id.tip)
    TextView tip;

    public static StatisticsDetailDataFragment newInstance() {
        StatisticsDetailDataFragment fragment=  new StatisticsDetailDataFragment();
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_statistics_data;
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
                case "StatisticsDetailDataFragment":
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



    private void setData() {

        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        IAxisValueFormatter xAxisFormatter = new MyYearAxisValueFormatter();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(5);
        xAxis.setValueFormatter(xAxisFormatter);

        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(5, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        BarDataSet set1 = new BarDataSet(entries, "历年脱贫分布");

            set1.setDrawIcons(false);
//          set1.setColors(ColorTemplate.MATERIAL_COLORS);

            /*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
            int endColor = ContextCompat.getColor(this, android.R.color.holo_blue_bright);
            set1.setGradientColor(startColor, endColor);*/
        set1.setColor(getResources().getColor(R.color.colorPrimary));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            //data.setValueTypeface(mTfLight);
            data.setBarWidth(0.8f);

            mChart.setData(data);
            mChart.invalidate();

    }

    @SuppressLint("CheckResult")
    public void loadData(){
        datas=new ArrayList<>();
        if(entries==null){
            entries=new ArrayList<>();
        }
        entries.clear();
        RetrofitHelper.getStatisticsAPI()
                .getOutPovertyNumber(StatisticsDetailActivity.permissionstag)
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
                            for (int i = 0; i < jsonArray.length(); i++) {
                                OutPovertyData outPovertyData=JSON.parseObject(jsonArray.get(i).toString(),OutPovertyData.class);
                                if(outPovertyData!=null){
                                    datas.add(outPovertyData);
                                    entries.add(new BarEntry(Float.parseFloat(outPovertyData.getTpnd()),Float.parseFloat(outPovertyData.getOutpoverty())));
                                }
                            }
                            if(entries.size()>0){
                                setData();
                                mChart.setVisibility(View.VISIBLE);
                                tip.setHint("");
                            }else{
                                mChart.setVisibility(View.INVISIBLE);
                                tip.setHint("没有查询到相关信息");
                            }

                            break;
                        case "-1":
                        case "-2":
                            mChart.setVisibility(View.INVISIBLE);
                            tip.setHint("没有查询到相关信息");
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                }, throwable -> {
                    mChart.setVisibility(View.INVISIBLE);
                    tip.setHint("没有查询到相关信息");
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
                });
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
