package com.dyhl.dusky.huangchuanfp.Utils.tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Name: isGetStringFromJson
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //对于json提取string的封装
 * Date: 2017-08-31 18:15
 */

public class isGetStringFromJson {
    public static String handleData(String key, String data){
        try{
            JsonObject myJsondata = new JsonParser().parse(data).getAsJsonObject();
            data=myJsondata.get(key).getAsString();
            return data;
        }catch (Exception e){
            return "";
        }

    }
}
