package com.dyhl.dusky.huangchuanfp.Utils.Chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter
{

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return (int)value + " 人";
    }
}
