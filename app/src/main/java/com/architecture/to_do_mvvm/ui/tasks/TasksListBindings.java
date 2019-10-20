package com.architecture.to_do_mvvm.ui.tasks;

import android.widget.ListView;

import androidx.databinding.BindingAdapter;

import com.architecture.to_do_mvvm.data.Task;

import java.util.List;

public class TasksListBindings {

    @BindingAdapter("app:items")
    public static void setItems(ListView listView, List<Task> items){
        TasksFragment.TasksAdapter adapter = (TasksFragment.TasksAdapter) listView.getAdapter();

        if(adapter != null){
            adapter.replaceData(items);
        }
    }
}
