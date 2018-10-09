package com.dyhl.dusky.huangchuanfp.Net.API;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Name: AnuncementService
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-05-07 12:30
 */

public interface StatisticsService {

    @POST("povertyCondition")
    @FormUrlEncoded
    Observable<ResponseBody> getpovertyCondition(@Field("code") String code);

    @POST("povertyReson")
    @FormUrlEncoded
    Observable<ResponseBody> getpovertyReson(@Field("code") String code);

    @POST("povertyType")
    @FormUrlEncoded
    Observable<ResponseBody> getpovertyType(@Field("code") String code);


    @POST("selectOutPovertyNumber")
    @FormUrlEncoded
    Observable<ResponseBody> getOutPovertyNumber(@Field("code") String code);
}
