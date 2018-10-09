package com.dyhl.dusky.huangchuanfp.Net.API;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
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

public interface PovertyProblemService {

    @POST("selectPovertyInfoReport")
    @FormUrlEncoded
    Observable<ResponseBody> getList(@Field("id") String id,
                                       @Field("code") String permissions,
                                       @Field("publics") String publics,
                                       @Field("currentPage") int currentPage,
                                       @Field("pageSize") int pageSize);

    @POST("selectPovertyReportDetail")
    @FormUrlEncoded
    Observable<ResponseBody> getDetail(@Field("num") String num);


    @POST("selectquestionclosely")
    @FormUrlEncoded
    Observable<ResponseBody> getQuestionList(@Field("num") String num,
                                     @Field("currentPage") int currentPage,
                                     @Field("pageSize") int pageSize);

    @POST("questionclosely")
    @FormUrlEncoded
    Observable<ResponseBody> Question(@Field("num") String num,
                                      @Field("value") String value);

    @POST("selectquestionreply")
    @FormUrlEncoded
    Observable<ResponseBody> getAnswerList(@Field("num") String num,
                                             @Field("currentPage") int currentPage,
                                             @Field("pageSize") int pageSize);

    @POST("questionreply")
    @FormUrlEncoded
    Observable<ResponseBody> Answer(@Field("num") String num,
                                    @Field("value") String value,
                                    @Field("userid") String userid);
}
