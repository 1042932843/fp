package com.dyhl.dusky.huangchuanfp.Net.API;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Name: PovertyListService
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2018-05-05 12:33
 */

public interface PovertyListService {

    @POST("selectPovertyFamilyInfo")
    @FormUrlEncoded
    Observable<ResponseBody> getList(@Field("id") String id,
                                   @Field("currentPage") String currentPage,
                                   @Field("pageSize") String pageSize);


    @POST("selectPovertyFamilyDetail")
    @FormUrlEncoded
    Observable<ResponseBody> getData(@Field("idcard") String code);

    @POST("selectPovertyFamilyMember")
    @FormUrlEncoded
    Observable<ResponseBody> getfamilyData(@Field("familyid") String code);

    @POST("selectLogByIdcard")
    @FormUrlEncoded
    Observable<ResponseBody> getLogData(@Field("idcard") String code,
                                        @Field("currentPage") String currentPage,
                                        @Field("pageSize") String pageSize);

    @POST("selectPartnerInfo")
    @FormUrlEncoded
    Observable<ResponseBody> getLiable(@Field("idcard") String code,
                                        @Field("currentPage") String currentPage,
                                        @Field("pageSize") String pageSize);
}
