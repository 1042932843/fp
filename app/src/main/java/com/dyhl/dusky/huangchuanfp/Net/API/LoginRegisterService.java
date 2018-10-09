package com.dyhl.dusky.huangchuanfp.Net.API;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Name: LoginRegisterService
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-09-30 16:33
 */

public interface LoginRegisterService {

    @POST("login")
    @FormUrlEncoded
    Observable<ResponseBody> login(@Field("account") String account,
                                   @Field("password") String password);

    @POST("updateUser")
    @FormUrlEncoded
    Observable<ResponseBody> reset(@Field("id") String id,
                                   @Field("oldpassword") String oldpassword,
                                   @Field("newpassword") String newpassword);

}
