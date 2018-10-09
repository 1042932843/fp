package com.dyhl.dusky.huangchuanfp.Net;


import com.dyhl.dusky.huangchuanfp.Base.DuskyApp;
import com.dyhl.dusky.huangchuanfp.Net.API.AnuncementService;
import com.dyhl.dusky.huangchuanfp.Net.API.ChoiceFragmentService;
import com.dyhl.dusky.huangchuanfp.Net.API.CommonListService;
import com.dyhl.dusky.huangchuanfp.Net.API.CommonService;
import com.dyhl.dusky.huangchuanfp.Net.API.ExperienceService;
import com.dyhl.dusky.huangchuanfp.Net.API.LogService;
import com.dyhl.dusky.huangchuanfp.Net.API.LoginRegisterService;
import com.dyhl.dusky.huangchuanfp.Net.API.PkcListService;
import com.dyhl.dusky.huangchuanfp.Net.API.PolicyService;
import com.dyhl.dusky.huangchuanfp.Net.API.PovertyListService;
import com.dyhl.dusky.huangchuanfp.Net.API.PovertyProblemService;
import com.dyhl.dusky.huangchuanfp.Net.API.StatisticsService;
import com.dyhl.dusky.huangchuanfp.Net.API.devService;
import com.dyhl.dusky.huangchuanfp.Utils.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Name: RetrofitHelper
 * Author: Dusky
 * QQ: 1042932843
 * Comment: //TODO
 * Date: 2017-09-09 15:30
 */
public class RetrofitHelper {

  private static OkHttpClient mOkHttpClient;

  static {
    initOkHttpClient();
  }

  public static devService getDataAPI() {
    return createApi(devService.class, ApiConstants.Base_URL);
  }

  public static LoginRegisterService getLoginRegisterAPI() {
    return createApi(LoginRegisterService.class, ApiConstants.Base_URL);
  }

  public static ChoiceFragmentService getChoiceFragmentAPI() {
    return createApi(ChoiceFragmentService.class, ApiConstants.Base_URL);
  }

  public static PovertyListService getPovertyListAPI() {
    return createApi(PovertyListService.class, ApiConstants.Base_URL);
  }
  public static AnuncementService getAnuncementAPI() {
    return createApi(AnuncementService.class, ApiConstants.Base_URL);
  }

  public static PovertyProblemService getPovertyProblemAPI() {
    return createApi(PovertyProblemService.class, ApiConstants.Base_URL);
  }

  public static CommonListService getCommonListAPI() {
    return createApi(CommonListService.class, ApiConstants.Base_URL);
  }

  public static StatisticsService getStatisticsAPI() {
    return createApi(StatisticsService.class, ApiConstants.Base_URL);
  }

  public static LogService getLogServiceAPI() {
    return createApi(LogService.class, ApiConstants.Base_URL);
  }

  public static PkcListService getPkcListServiceAPI() {
    return createApi(PkcListService.class, ApiConstants.Base_URL);
  }

  public static PolicyService getPolicyServiceAPI() {
    return createApi(PolicyService.class, ApiConstants.Base_URL);
  }

  public static ExperienceService getExperienceServiceAPI() {
    return createApi(ExperienceService.class, ApiConstants.Base_URL);
  }

  public static CommonService getCommonServiceAPI() {
    return createApi(CommonService.class, ApiConstants.Base_URL);
  }

  /**
   * 根据传入的baseUrl，和api创建retrofit
   */
  private static <T> T createApi(Class<T> clazz, String baseUrl) {

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(mOkHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    return retrofit.create(clazz);
  }

  /**
   * 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志,设置UA拦截器
   */
  private static void initOkHttpClient() {

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    //StatusInterceptor statusInterceptor=new StatusInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    if (mOkHttpClient == null) {
      synchronized (RetrofitHelper.class) {
        if (mOkHttpClient == null) {
          //设置Http缓存
          Cache cache = new Cache(new File(DuskyApp.getInstance()
              .getCacheDir(), "HttpCache"), 1024 * 1024 * 10);

          mOkHttpClient = new OkHttpClient.Builder()
              .cache(cache)
              //.addInterceptor(interceptor).addInterceptor(statusInterceptor)
             // .addNetworkInterceptor(new CacheInterceptor())//这里关闭缓存
              .retryOnConnectionFailure(true)
              .connectTimeout(20, TimeUnit.SECONDS)
              .writeTimeout(20, TimeUnit.SECONDS)
              .readTimeout(20, TimeUnit.SECONDS)
              //.addInterceptor(new UserAgentInterceptor())
              .build();
        }
      }
    }
  }


  /**
   * 添加UA拦截器

  private static class UserAgentInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

      Request originalRequest = chain.request();
      Request requestWithUserAgent = originalRequest.newBuilder()
          .removeHeader("User-Agent")
          .addHeader("User-Agent", ApiConstants.COMMON_UA_STR)
          .build();
      return chain.proceed(requestWithUserAgent);
    }
  } */

  /**
   * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
   */
  private static class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

      // 有网络时 设置缓存超时时间1个小时
      int maxAge = 60 * 60;
      // 无网络时，设置超时为1天
      int maxStale = 60 * 60 * 24;
      Request request = chain.request();
      if (CommonUtil.isNetworkAvailable(DuskyApp.getInstance())) {
        //有网络时只从网络获取
        request = request.newBuilder()
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build();
      } else {
        //无网络时只从缓存中读取
        request = request.newBuilder()
            .cacheControl(CacheControl.FORCE_CACHE)
            .build();
      }
      Response response = chain.proceed(request);
      if (CommonUtil.isNetworkAvailable(DuskyApp.getInstance())) {
        response = response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", "public, max-age=" + maxAge)
            .build();
      } else {
        response = response.newBuilder()
            .removeHeader("Pragma")
            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
            .build();
      }
      return response;
    }
  }
}
