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
public interface devService {

    @POST
    @FormUrlEncoded
    Observable<ResponseBody> getData(@Url String url, @FieldMap Map<String, Object> map);
}
