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
public interface CommonService {

    @POST("setupPublics")
    @FormUrlEncoded
    Observable<ResponseBody> setupPublics(@Field("type") int type,//1：上报问题，2：日志，3：通知公告和新闻政策
                                          @Field("id") String expid,
                                             @Field("publics") String publics);
}
