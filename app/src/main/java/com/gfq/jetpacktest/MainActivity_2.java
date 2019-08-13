package com.gfq.jetpacktest;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_2 extends AppCompatActivity {
    ListBaseAdapter<News.ResultBean> adapter;
    List<News.ResultBean> list;
    WebView webView;
    TextView close;
    RelativeLayout rlWeb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        webView = findViewById(R.id.webView);
        //设置自适应屏幕，两者合用
        webView.getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        webView.getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.getSettings().setJavaScriptEnabled(true);
        //缩放操作
        webView.getSettings().setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webView.getSettings().setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webView.getSettings().setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webView.getSettings().setAllowFileAccess(true); //设置可以访问文件
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webView.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        webView.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
        close = findViewById(R.id.tv_close);
        rlWeb = findViewById(R.id.rl_web);
        list = new ArrayList<>();
        //ActivityItemBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_item);
        init();
        adapter = new ListBaseAdapter<News.ResultBean>(this, BR.news) {
            @Override
            public void setPresentor(SuperViewHolder holder, int position) {
                holder.getBinding().setVariable(BR.presenter, new Presenter(position));
            }

            @Override
            public int getLayoutId() {
                return R.layout.activity_item2;
            }
        };

        ((RecyclerView) findViewById(R.id.rv)).setAdapter(adapter);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rlWeb.isShown()){
                    rlWeb.setVisibility(View.GONE);
                    webView.onPause();
                }
            }
        });
    }


    private void init() {
        HttpUtil.executeMethod(HttpUtil.api().getWangYiNews("1", "20"), new HttpUtil.OnCallBack<News>() {
            @Override
            public void onSucceed(News data) {
                list = data.getResult();
                adapter.setDataList(list);
            }
        });
    }

    public class Presenter {
        int position;



        public Presenter(int position) {
            this.position = position;
        }

        public void openWebView(View view){
                rlWeb.setVisibility(View.VISIBLE);
                webView.onResume();
                webView.loadUrl(adapter.getDataList().get(position).getPath());
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
