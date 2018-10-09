package com.dyhl.dusky.huangchuanfp.Utils.tools;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Name: isGetJsonArrayFromJson
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //对于json提取string的封装
 * Date: 2017-08-31 18:15
 */

public class isGetJsonArrayFromJson {
    public static JsonArray handleData(String key, String data){
        JsonArray array=new JsonArray();
        try{
            JsonObject myJsondata = new JsonParser().parse(data).getAsJsonObject();
            array=myJsondata.get(key).getAsJsonArray();
            return array;
        }catch (Exception e){
            return array;
        }

    }
}
