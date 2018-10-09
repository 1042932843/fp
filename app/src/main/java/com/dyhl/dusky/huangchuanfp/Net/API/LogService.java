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
 * @TIME: 2018/5/18
 * @DESCRIPTION:@Url String url
 */
public interface LogService {

    @POST("logStatistics")
    @FormUrlEncoded
    Observable<ResponseBody> logStatistics(@Field("type") String type,
                                   @Field("code") String code,
                                   @Field("town") String town,
                                   @Field("village") String village,
                                   @Field("name") String name,
                                   @Field("start") String start,
                                   @Field("end") String end,
                                   @Field("currentPage") String currentPage,
                                   @Field("pageSize") String pageSize);
}
