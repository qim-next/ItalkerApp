package com.example.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements View.OnClickListener,
        View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    /**
     * 构造函数模块
     */
    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        mListener = listener;
    }

    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，复写后返回的都是XML的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     * * @param position 坐标
     *
     * @param data 当前的数据
     * @return 界面的类型，XML文件的ID
     */
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面类型, 约定为xml的布局ID
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 得到LayoutInflate用于把XML初始化为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // 把 XML id为viewType的文件初始化为rootView
        View root = inflater.inflate(viewType, parent, false);
        // 通过子类必须实现的方法，得到ViewHolder
        ViewHolder<Data> holder = onCreateHolder(root, viewType);

        // 设置点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        // 设置View的tag为Tagholder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);

        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);

        // 绑定callback
        holder.callback = this;
        return holder;
    }

    /**
     * 当得到一个新的ViewHolder
     * * @param root 根布局
     *
     * @param viewType 布局类型，XML的ID
     * @return
     */
    protected abstract ViewHolder<Data> onCreateHolder(View root, int viewType);

    /**
     * 绑定数据到ViewHolder
     *
     * @param holder   ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 得到需要绑定的数据
        Data data = mDataList.get(position);
        // 触发数据的绑定
        holder.bind(data);
    }


    /**
     * 得到当前集合的数据量
     * * @return
     */
    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * 插入并通知插入
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据并且通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据并更新
     *
     * @param dataList
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }


    /**
     * 删除操作
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     * * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if(this.mListener != null){
            int pos = holder.getAdapterPosition();
            this.mListener.onItemClick(holder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到当前ViewHolder当前对应的适配器的坐标
            int pos = holder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemLongClick(holder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 设置适配器的监听
     *
     * @param listener AdapterListener
     */
    public void setListener(AdapterListener<Data> listener) {
        this.mListener = listener;
    }

    /**
     * 自定义监听器
     *
     * @param <Data> 范型
     */
    public interface AdapterListener<Data> {
        // 当cell点击时触发
        void onItemClick(RecyclerView.ViewHolder holder, Data data);

        // 当cell长按的时候触发
        void onItemLongClick(RecyclerView.ViewHolder holder, Data data);
    }

    /**
     * 自定义的ViewHolder
     *
     * @param <Data> 泛型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        AdapterCallback<Data> callback;
        private Unbinder unbinder;
        protected Data mData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
        }

        /**
         * 当触发绑定数据的时候，回调，必须复写
         *
         * @param data
         */
        protected abstract void onBind(Data data);

        /**
         * Holder自己对自己的更新操作
         *
         * @param data
         */
        public void updateData(Data data) {
            if (callback != null) {
                this.callback.update(data, this);
            }
        }
    }
}
