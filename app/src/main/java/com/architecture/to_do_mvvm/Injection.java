package com.architecture.to_do_mvvm;

import android.content.Context;

import androidx.annotation.NonNull;

import com.architecture.to_do_mvvm.data.source.TasksRepository;
import com.architecture.to_do_mvvm.data.source.local.TasksLocalDataSource;
import com.architecture.to_do_mvvm.data.source.local.ToDoDatabase;
import com.architecture.to_do_mvvm.util.AppExecutors;
import com.google.common.base.Preconditions;

public class Injection {

    public static TasksRepository provideTaskRepository(@NonNull Context context){
        Preconditions.checkNotNull(context);

        ToDoDatabase doDatabase = ToDoDatabase.getInstance(context);
        return TasksRepository.getInstance(TasksLocalDataSource
                .getInstance(new AppExecutors(), doDatabase.getTaskDao()));
    }
}
