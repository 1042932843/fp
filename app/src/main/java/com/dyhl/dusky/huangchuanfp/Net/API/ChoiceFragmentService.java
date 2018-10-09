package com.dyhl.dusky.huangchuanfp.Net.API;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Name: ChoiceFragmentService
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-05-05 12:30
 */

public interface ChoiceFragmentService {

    @POST("selectPartnerInfoByPeople")
    @FormUrlEncoded
    Observable<ResponseBody> getPartnerInfo(@Field("id") String id,
                                   @Field("currentPage") String currentPage,
                                   @Field("pageSize") String pageSize);

}
