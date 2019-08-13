package com.gfq.jetpacktest;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//HttpUtil.executeMethod(HttpUtil.createApi(HttpApi.class).registerUser(apikey, "test01", "123456"), new HttpUtil.OnCallBack<RespUserInfo>() {
//            @Override
//            public void onSucceed(RespUserInfo data) {
//                Log.d("gggg", "onSucceed: ");
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//        });

public class HttpUtil {
    public static final String BASE_URL = "https://api.apiopen.top/";
    private static Retrofit retrofit;
    private static HttpApi httpApi;

    static {
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpApi = retrofit.create(HttpApi.class);
    }


    public static HttpApi api (){
        return httpApi;
    }
  //  public static <T> T createApi(Class<T> clazz) {
   //     return retrofit.create(clazz);
   // }
    //HttpApi httpApi = retrofit.create(HttpApi.class);


    public static <T> void executeMethod(Observable<T> observable, final OnCallBack<T> onCallBack) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<T>() {
                    @Override
                    public void onNext(T o) {
                        onCallBack.onSucceed(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                       // onCallBack.onError(e);
                    }

                    @Override
                    public void onComplete() {
                       // onCallBack.onCompleted();
                    }
                });

    }

    public interface OnCallBack<T> {
        void onSucceed(T data);



    }


}
