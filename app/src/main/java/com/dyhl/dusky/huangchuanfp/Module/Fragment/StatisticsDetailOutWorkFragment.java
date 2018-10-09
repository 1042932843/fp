package com.dyhl.dusky.huangchuanfp.Module.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhl.dusky.huangchuanfp.Base.BaseFragment;
import com.dyhl.dusky.huangchuanfp.Module.StatisticsDetailActivity;
import com.dyhl.dusky.huangchuanfp.Module.entity.ApiMsg;
import com.dyhl.dusky.huangchuanfp.Module.entity.PovertyCondition;
import com.dyhl.dusky.huangchuanfp.Module.entity.WorkType;
import com.dyhl.dusky.huangchuanfp.Module.entity.Reson;
import com.dyhl.dusky.huangchuanfp.Net.RetrofitHelper;
import com.dyhl.dusky.huangchuanfp.R;
import com.dyhl.dusky.huangchuanfp.Utils.ToastUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class StatisticsDetailOutWorkFragment extends BaseFragment implements OnChartValueSelectedListener {
    ArrayList<WorkType> workTypes;
    ArrayList<PieEntry> entries;
    @BindView(R.id.PieChart)
    PieChart mChart;

    @BindView(R.id.tip)
    TextView tip;

    public static StatisticsDetailOutWorkFragment newInstance() {
        StatisticsDetailOutWorkFragment fragment=  new StatisticsDetailOutWorkFragment();
        return fragment;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_statistics_outwork;
    }


    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        //loadData();
        initPieChart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String event){
        if(!TextUtils.isEmpty(event)) {
            switch (event) {
                case "StatisticsDetailOutWorkFragment":
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

    private void initPieChart() {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        //mChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        //mChart.setCenterText(generateCenterSpannableText());

        mChart.setExtraOffsets(30.f, 0.f, 30.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(32f);
        mChart.setTransparentCircleRadius(48f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        //mChart.animateY(1400, Easing.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    private void setData() {

        PieDataSet dataSet = new PieDataSet(entries, "务工人员统计");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.4f);
        dataSet.setValueLinePart2Length(0.8f);
        //dataSet.setSelectionShift(0f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.BLACK);
        //data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    float largest=-1;
    @SuppressLint("CheckResult")
    public void loadData(){
        largest=-1;
        workTypes=new ArrayList<>();
        entries = new ArrayList<PieEntry>();
        RetrofitHelper.getStatisticsAPI()
                .getpovertyCondition(StatisticsDetailActivity.permissionstag)
                .compose(this.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    String a=bean.string();
                    ApiMsg apiMsg = JSON.parseObject(a,ApiMsg.class);
                    String state = apiMsg.getState();
                    switch (state){
                        case "0":
                            PovertyCondition povertyInformation= JSON.parseObject(apiMsg.getResult(),PovertyCondition.class);
                            if(povertyInformation==null){
                                tip.setHint("没有查询到相关信息");
                                break;
                            }

                            if(povertyInformation.getOutwork()!=null){
                                WorkType workType=new WorkType();
                                workType.setType("外出务工人员");
                                workType.setNum(Integer.parseInt(povertyInformation.getOutwork()));
                                workTypes.add(workType);
                                entries.add(new PieEntry((float)workType.getNum(),workType.getType()+"("+workType.getNum()+")"));
                            }
                            if(povertyInformation.getTotalpopulation()!=null&&povertyInformation.getOutwork()!=null){
                                WorkType workType=new WorkType();
                                workType.setType("本地务工人员");
                                workType.setNum(Integer.parseInt(povertyInformation.getTotalpopulation())-Integer.parseInt(povertyInformation.getOutwork()));
                                workTypes.add(workType);
                                entries.add(new PieEntry((float)workType.getNum(),workType.getType()+"("+workType.getNum()+")"));
                            }
                            if(entries.size()>0){
                                setData();
                                tip.setHint("");
                                mChart.setVisibility(View.VISIBLE);
                            }else {
                                tip.setHint("没有查询到相关信息");
                                mChart.setVisibility(View.INVISIBLE);
                            }


                            break;
                        case "-1":
                        case "-2":
                            tip.setHint("没有查询到相关信息");
                            mChart.setVisibility(View.INVISIBLE);
                            ToastUtil.ShortToast(apiMsg.getMessage());
                            break;
                    }

                }, throwable -> {
                    tip.setHint("没有查询到相关信息");
                    mChart.setVisibility(View.INVISIBLE);
                    ToastUtil.ShortToast("返回错误，请确认网络正常或服务器正常");
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
