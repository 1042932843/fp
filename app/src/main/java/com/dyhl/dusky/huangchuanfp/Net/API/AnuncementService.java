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

public interface AnuncementService {

    @POST("selectNoticeDetail")
    @FormUrlEncoded
    Observable<ResponseBody> getAnuncementDetail(@Field("id") String id);


    @POST("saveNotice")
    @FormUrlEncoded
    Observable<ResponseBody> push(@Field("title") String title,
                                  @Field("value") String value,
                                  @Field("source") String source,
                                  @Field("type") String type,
                                  @Field("code") String code,
                                  @Field("level") int level
                                  );

    @POST("updateNotice")
    @FormUrlEncoded
    Observable<ResponseBody> update(@Field("id") String id,
                                  @Field("title") String title,
                                  @Field("value") String value,
                                  @Field("source") String source,
                                  @Field("type") String type,
                                  @Field("code") String code
    );
}
