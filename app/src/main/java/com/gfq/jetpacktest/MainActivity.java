package com.gfq.jetpacktest;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gfq.jetpacktest.databinding.ActivityItemBinding;


/**
 * dataBinding + liveData + viewModel
 */
public class MainActivity extends AppCompatActivity {
    private ActivityItemBinding binding;
    private MyViewModel myViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_item);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);


        myViewModel.getNews().observe(this, new Observer<News>() {
            @Override
            public void onChanged(News news) {
                binding.setNews(news);
               // setTitle(news.getMessage());
            }
        });
        binding.setPresenter(new Presenter());

        binding.setLifecycleOwner(this);

    }



    public class Presenter {

        public void onClick(View view) {
            Log.d("Presenter", "onClick: ");
            Toast.makeText(MainActivity.this, "hhh", Toast.LENGTH_SHORT).show();
        }
        public void to(View view) {
           startActivity(new Intent(MainActivity.this,MainActivity_2.class));
        }



    }



}
