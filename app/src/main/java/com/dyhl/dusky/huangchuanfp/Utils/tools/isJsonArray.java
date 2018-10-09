package com.dyhl.dusky.huangchuanfp.Utils.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Name: isJsonArray
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //jsonArray单元
 * Date: 2017-08-24 10:27
 */

public class isJsonArray {
    //获取单列的array
    public static JsonArray handleData(String key, String data) {
        JsonArray array = new JsonArray();
        try {
            JsonObject myJsondata = new JsonParser().parse(data).getAsJsonObject();
            array = myJsondata.getAsJsonArray(key);
            return array;
        } catch (Exception e) {
            return array;
        }

    }
}