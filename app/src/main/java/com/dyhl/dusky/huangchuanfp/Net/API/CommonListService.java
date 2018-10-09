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

public interface CommonListService {

    //责任组list
    @POST("selectGjzrz")
    @FormUrlEncoded
    Observable<ResponseBody> getZRZList(@Field("code") String code,
                                        @Field("phone") String phone,
                                        @Field("name") String name,
                                        @Field("currentPage") int currentPage,
                                        @Field("pageSize") int pageSize);

    //工作队list
    @POST("selectZcgzd")
    @FormUrlEncoded
    Observable<ResponseBody> getGZDList(@Field("code") String code,
                                         @Field("phone") String phone,
                                         @Field("name") String name,
                                         @Field("currentPage") int currentPage,
                                         @Field("pageSize") int pageSize);
    //第一书记list
    @POST("selectMajorSecretary")
    @FormUrlEncoded
    Observable<ResponseBody> getDYSJList(@Field("code") String code,
                                         @Field("phone") String phone,
                                         @Field("name") String name,
                                         @Field("currentPage") int currentPage,
                                         @Field("pageSize") int pageSize);


    //贫困村list
    @POST("selectdepartment")
    @FormUrlEncoded
    Observable<ResponseBody> getBFDWList(@Field("id") String id,
                                        @Field("code") String code,
                                        @Field("departmentname") String departmentname,
                                        @Field("incharge") String incharge,
                                        @Field("contact") String contact,
                                        @Field("phone") String phone,
                                        @Field("currentPage") int currentPage,
                                        @Field("pageSize") int pageSize
    );

    //贫困村list
    @POST("selectVillageinfo")
    @FormUrlEncoded
    Observable<ResponseBody> getPKCList(@Field("code") String code,
                                        @Field("town") String town,
                                        @Field("village") String village,
                                        @Field("outpoverty") String isoutpoverty,
                                        @Field("currentPage") int currentPage,
                                        @Field("pageSize") int pageSize
                                        );

    //贫困村list
    @POST("selectVillageinfo")
    @FormUrlEncoded
    Observable<ResponseBody> getPKCList(@Field("code") String code,
                                        @Field("departmentid") String departmentid,
                                        @Field("currentPage") int currentPage,
                                        @Field("pageSize") int pageSize
    );

    //贫困户list
    @POST("checkPovertyFamily")
    @FormUrlEncoded
    Observable<ResponseBody> getPKHList(@Field("code") String permissions,
                                        @Field("name") String name,
                                        @Field("responsible") String responsible,
                                        @Field("povertyattribute") String povertyattribute,
                                        @Field("overcomeattribute") String overcomeattribute,
                                        @Field("reson") String reson,
                                        @Field("currentPage") int currentPage,
                                        @Field("pageSize") int pageSize);

    //帮扶责任人list
    @POST("checkresponsible")
    @FormUrlEncoded
    Observable<ResponseBody> getZZRList( @Field("code") String permissions,
                                         @Field("name") String name,
                                         @Field("position") String position,
                                         @Field("phone") String phone,
                                         @Field("familyid") String familyid,
                                         @Field("currentPage") int currentPage,
                                         @Field("pageSize") int pageSize);

    //帮扶责任人list
    @POST("checkresponsible")
    @FormUrlEncoded
    Observable<ResponseBody> getZZRList( @Field("code") String permissions,
                                         @Field("departmentid") String departmentid,
                                         @Field("currentPage") int currentPage,
                                         @Field("pageSize") int pageSize);

    //帮扶日志list
    @POST("checkLog")
    @FormUrlEncoded
    Observable<ResponseBody> getBFRZList( @Field("publics") String publics,
                                         @Field("code") String permissions,
                                         @Field("name") String name,
                                         @Field("responsible") String responsible,
                                         @Field("start") String start,
                                         @Field("end") String end,
                                         @Field("currentPage") int currentPage,
                                         @Field("pageSize") int pageSize);

    //上报问题list
    @POST("checkPovertyInfoReport")
    @FormUrlEncoded
    Observable<ResponseBody> getSBWTList(@Field("code") String permissions,
                                         @Field("name") String name,
                                         @Field("responsible") String responsible,
                                         @Field("start") String start,
                                         @Field("end") String end,
                                         @Field("publics") String publics,
                                         @Field("reply") String reply,
                                         @Field("currentPage") int currentPage,
                                         @Field("pageSize") int pageSize);

    //贫困属性比如：贫困
    @POST("overpovertyType")
    @FormUrlEncoded
    Observable<ResponseBody> getoverpovertyType(@Field("code") String permissions);

    //贫困类型比如：一般农户
    @POST("povertyType")
    @FormUrlEncoded
    Observable<ResponseBody> getPoertyType(@Field("code") String permissions);

    //贫困原因比如：穷
    @POST("povertyReson")
    @FormUrlEncoded
    Observable<ResponseBody> getPovertyReson(@Field("code") String permissions);

    @POST("selectArea2")
    @FormUrlEncoded
    Observable<ResponseBody> getArea(@Field("code") String permissions);

    @POST("selectLogDetail")
    @FormUrlEncoded
    Observable<ResponseBody> getLog(@Field("num") String num);
}
