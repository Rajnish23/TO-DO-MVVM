package com.architecture.to_do_mvvm.ui.taskdetail;

import android.content.Context;

import androidx.annotation.Nullable;

import com.architecture.to_do_mvvm.TaskViewModel;
import com.architecture.to_do_mvvm.data.source.TasksRepository;

public class TaskDetailViewModel extends TaskViewModel {

    @Nullable
    private TaskDetailNavigator mTaskDetailNavigator;

    public TaskDetailViewModel(Context context, TasksRepository tasksRepository){
        super(tasksRepository, context);
    }

    public void setNavigator(TaskDetailNavigator navigator){
        mTaskDetailNavigator = navigator;
    }

    public void onActivityDestroyed(){
        //Clear potentials to avoid memory leaks
        mTaskDetailNavigator = null;
    }

    public void deleteTask(){
        super.deleteTask();
        if(mTaskDetailNavigator != null){
            mTaskDetailNavigator.onTaskDeleted();
        }
    }

    public void startEditTask(){
        if(mTaskDetailNavigator != null){
            mTaskDetailNavigator.onStartEditTask();
        }
    }
}
