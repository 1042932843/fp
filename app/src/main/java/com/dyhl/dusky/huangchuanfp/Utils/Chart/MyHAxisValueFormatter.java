package com.dyhl.dusky.huangchuanfp.Utils.Chart;

import com.dyhl.dusky.huangchuanfp.Module.entity.Reson;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class MyHAxisValueFormatter implements IAxisValueFormatter
{
    ArrayList<Reson> types=new ArrayList<>();

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(types.size()>0){
            if(value==0){
                return types.get((int)(0)).getReson();
            }else{
                if(value/20<types.size()){
                    return types.get((int)(value/20)).getReson();
                }else {
                    return "";
                }

            }

        }else {
            return value+"";
        }

    }

    public void setTypes(ArrayList<Reson> types){
        if(types!=null){
            this.types=types;
        }
    }
}
