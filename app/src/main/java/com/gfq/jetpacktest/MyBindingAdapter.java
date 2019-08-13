package com.gfq.jetpacktest;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class MyBindingAdapter  {

    @BindingAdapter({"url"})
    public static void setImgUrl(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }


}
