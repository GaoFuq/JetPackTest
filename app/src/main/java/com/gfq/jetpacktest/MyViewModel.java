package com.gfq.jetpacktest;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<News> newsMutableLiveData;

    public MyViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<News> getNews() {
        if (newsMutableLiveData == null) {
            newsMutableLiveData = new MutableLiveData<>();
            init();
        }
        return newsMutableLiveData;
    }


    private void init() {
        HttpUtil.executeMethod(HttpUtil.api().getWangYiNews("1", "5"), new HttpUtil.OnCallBack<News>() {
            @Override
            public void onSucceed(News data) {
                newsMutableLiveData.setValue(data);
            }
        });
    }
}
