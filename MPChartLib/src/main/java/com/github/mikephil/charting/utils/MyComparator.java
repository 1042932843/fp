package com.github.mikephil.charting.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/6/15
 * @DESCRIPTION:
 */
public class MyComparator implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject o1, JSONObject o2) {
        String key1 = null;
        try {
            key1 = o1.getString("num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String key2 = null;
        try {
            key2 = o2.getString("num");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int int1 = Integer.parseInt(key1);
        int int2 = Integer.parseInt(key2);

        /*
        if(int1>int2){
            return 1;
        }
        return 0;
        */
        return int1-int2;
    }


}
