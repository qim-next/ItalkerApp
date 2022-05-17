package com.example.androidtest;

import android.os.Bundle;
import android.widget.TextView;

import com.example.common.app.Activity;

import butterknife.BindView;

public class MainActivity extends Activity {
    @BindView(R.id.text_test)
    TextView mText;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mText.setText("Hello Android！！！");
    }
}