package com.architecture.to_do_mvvm.util;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.architecture.to_do_mvvm.ui.tasks.TasksViewModel;

public class SwipeRefreshLayoutDataBinding {

    @BindingAdapter("android:onRefresh")
    public static void  setSwipeRefreshLayoutOnRefreshListener(final ScrollChildSwipeRefreshLayout view,
                                                               final TasksViewModel viewModel){
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.loadTasks(true);
            }
        });
    }
}
