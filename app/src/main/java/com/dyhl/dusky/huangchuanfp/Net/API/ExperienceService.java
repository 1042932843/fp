package com.dyhl.dusky.huangchuanfp.Net.API;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @AUTHOR: dsy
 * @TIME: 2018/6/4
 */
public interface ExperienceService {

    @POST("selectExperience")
    @FormUrlEncoded
    Observable<ResponseBody> getList(@Field("singlekey") String singlekey,
                                     @Field("totalkey") String totalkey,
                                     @Field("code") String code,
                                     @Field("title") String title,
                                     @Field("value") String value,
                                     @Field("name") String name,
                                     @Field("publics") String publics,
                                     @Field("start") String start,
                                     @Field("end") String end,
                                     @Field("currentPage") int currentPage,
                                     @Field("pageSize") int pageSize);

    @POST("setupExperience")
    @FormUrlEncoded
    Observable<ResponseBody> setupExperience(@Field("expid") String expid,
                                             @Field("publics") String publics);

    @POST("selectExperienceDetail")
    @FormUrlEncoded
    Observable<ResponseBody> selectExperienceDetail(@Field("expid") String expid);
}
