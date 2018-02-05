package com.shy.yimanage.ShyControls.Shylist.Recy;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;

/**
 * @User: shy
 * @Date: 18/2/1
 * @Time: 下午4:36
 * @Describe: recycler Fragment版本的列表
 */
public class ShyRecyclerListFragment extends Fragment {

    //设置Holder
    public interface OnShyRecyclerListFragment {
        //返回设置holder
        public void onBindViewHolder(View view, int position, JSONArray array);
    }

    //刷新
    public interface OnRefresh {
        public void Refresh();
    }

    //加载
    public interface OnLoading {
        public void Loading(int pages);
    }

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    static OnShyRecyclerListFragment onShyRecyclerListFragment;
    ShyAdapte shyAdapte;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(lp);

        swipeRefreshLayout = new SwipeRefreshLayout(getActivity());
        ViewGroup.LayoutParams svp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        swipeRefreshLayout.setLayoutParams(svp);
        linearLayout.addView(swipeRefreshLayout);

        recyclerView = new RecyclerView(getActivity());
        ViewGroup.LayoutParams rvp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(rvp);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.addView(recyclerView);
        return linearLayout;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * @param layout   ViewHolder 布局
     * @param callback 回父类Activity 操作函数
     */
    public void setItemView(@NonNull int layout, @NonNull OnShyRecyclerListFragment callback, @Nullable final OnLoading onLoading, @Nullable final OnRefresh onRefresh) {

        onShyRecyclerListFragment = callback;
        shyAdapte = new ShyAdapte(getActivity(), layout);
        recyclerView.setAdapter(shyAdapte);

        //下拉刷新
        if (onRefresh != null) {
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                public void onRefresh() {
                    onRefresh.Refresh();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
        //加载更多
        if (onLoading != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    //当前RecyclerView显示出来的最后一个的item的position
                    int lastPosition = -1;

                    //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof GridLayoutManager) {
                            //通过LayoutManager找到当前显示的最后的item的position
                            lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                        } else if (layoutManager instanceof LinearLayoutManager) {
                            lastPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                            //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                            //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                            int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                            lastPosition = findMax(lastPositions);
                        }

                        //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                        //如果相等则说明已经滑动到最后了
                        if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
//                            Toast.makeText(getActivity(), "滑动到底了", Toast.LENGTH_SHORT).show();
                            onLoading.Loading(shyAdapte.pages);
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                }

            });
        }
    }

    public ShyAdapte getShyAdapte() {
        return shyAdapte;
    }

    /*
    * Adapter
    * */
    static class ShyAdapte extends RecyclerView.Adapter {
        Context context;
        JSONArray array;
        int layout;
        public int pages = 1;

        public ShyAdapte(Context context, int layout) {
            super();
            this.context = context;
            this.layout = layout;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            ShyHolder shyHolder = new ShyHolder(v);
            return shyHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ShyHolder shyholder = (ShyHolder) holder;
            onShyRecyclerListFragment.onBindViewHolder(shyholder.view, position, array);
        }

        @Override
        public int getItemCount() {
            return array.length();
        }

        //重置列表
        public void setDataArray(@NonNull JSONArray jsonArray) {
            if (jsonArray != null) {
                this.array = jsonArray;
                notifyDataSetChanged();
            }
        }

        //增加列表
        public void addDataArray(JSONArray jsonArray) {
            pages++;
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    this.array.put(jsonArray.opt(i));
                }
            }
            notifyDataSetChanged();
        }
    }


    /**
     * holder
     */
    static class ShyHolder extends RecyclerView.ViewHolder {
        public View view;

        public ShyHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
