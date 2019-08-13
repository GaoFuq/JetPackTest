package com.gfq.jetpacktest;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author 高富强
 * createDate 2019/7/5 14:45
 * 作用： api接口统一管理
 */
public interface HttpApi {


    @FormUrlEncoded
    @POST("https://api.apiopen.top/getWangYiNews")
    Observable<News> getWangYiNews(@Field("page") String page, @Field("count") String count);
}
