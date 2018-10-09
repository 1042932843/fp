package com.dyhl.dusky.huangchuanfp.Net;
import com.google.gson.JsonObject;

/**
 * Name: RequestProperty
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-10-09 14:22
 */

public class RequestProperty {
    public static JsonObject CreateJsonObjectBody(){
        JsonObject object=new JsonObject();
        //object.addProperty("ver", appConfig.versionName);

        return object;
    }

    public static JsonObject CreateTokenJsonObjectBody(){
        JsonObject object=new JsonObject();
        //object.addProperty("ver", appConfig.versionName);

        return object;
    }
}
