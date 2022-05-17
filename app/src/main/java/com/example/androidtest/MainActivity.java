package com.example.androidtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.common.Common;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Common();
    }
}