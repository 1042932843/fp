package com.dyhl.dusky.huangchuanfp.Utils.tools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Name: isJsonObj
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //obj单元
 * Date: 2017-08-24 11:31
 */

public class isJsonObj {

    public static String handleData(String key, String data){
        try {
            JsonObject myJsondata = new JsonParser().parse(data).getAsJsonObject();
            data=myJsondata.getAsJsonObject(key).toString();
            return data;
        }catch (Exception e){
            return "";
        }
    }

}
