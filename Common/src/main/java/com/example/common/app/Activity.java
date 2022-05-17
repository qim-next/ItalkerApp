package com.example.common.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.List;

import butterknife.ButterKnife;

public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(initArgs(getIntent().getExtras()))
        {
            getContentLayoutId();
            initWidget();
            initData();
        }else{
            finish();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWidows(){
    }

    /**
     * 初始化相关参数
     * @param bundle
     * @return 如果参数正确返回true，错误放回false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    /**
     * 得到当前资源文件的ID
     * @return
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget(){

    }

    /**
     * 初始化数据
     */
    protected void initData(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        // 当点击界面导航返回时，finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        // 得到当前Activity下的所有fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if(fragments != null && fragments.size() > 0){
            for(Fragment fragment : fragments){
                if(fragment instanceof com.example.common.app.Fragment){
                    if(((com.example.common.app.Fragment)fragment).onPressed()){ // 判断是否拦截返回按钮
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
    }
}
