package com.example.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class Fragment extends androidx.fragment.app.Fragment {
    protected View mRoot;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    protected void initArgs(Bundle arguments){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            int layId = getContentLayoutId();
            // 初始化当前根布局，但是不再创建时就添加到container中
            View root = inflater.inflate(layId, container, false);
            initWidget(root);
            mRoot = root;
        } else {
            // 把当前root从父布局移除
           if(mRoot.getParent() != null){
               ((ViewGroup)mRoot.getParent()).removeView(mRoot);
           }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    /**
     * 得到当前界面的资源文件ID
     *
     * @return
     */
    protected abstract int getContentLayoutId();

    protected void initWidget(View root) {

    }

    protected void initData() {

    }

    /**
     * 返回按键触发时调用
     * @return 返回true代表在fragmen中处理，无需activity处理
     * 返回false代表fragment未处理back事件交由activity处理
     */
    public boolean onPressed(){
        return false;
    }
}
