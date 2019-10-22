package com.architecture.to_do_mvvm.data.source.local;

import androidx.annotation.NonNull;

import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.data.source.TasksDataSource;
import com.architecture.to_do_mvvm.util.AppExecutors;
import com.google.common.base.Preconditions;

import java.util.List;

import static com.google.common.base.Preconditions.*;

public class TasksLocalDataSource implements TasksDataSource {

    private static volatile TasksLocalDataSource INSTANCE;

    private TasksDao mTasksDao;

    private AppExecutors mAppExecutors;

    //Prevent direct instantiation
    private TasksLocalDataSource(@NonNull TasksDao mTasksDao, @NonNull AppExecutors mAppExecutors) {
        this.mTasksDao = mTasksDao;
        this.mAppExecutors = mAppExecutors;
    }

    public static TasksLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull TasksDao tasksDao) {
        if (INSTANCE == null) {
            synchronized (TasksLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TasksLocalDataSource(tasksDao, appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note : {@link LoadTasksCallback#onDataNotAvailable()} is fired if the database doesn't exists
     *          or table is empty
     */
    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = mTasksDao.getTasks();
                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        //Imp
                        callback.onTasksLoaded(tasks);
                    }
                });
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    /**
     * Note : {@link GetTaskCallback#onDataNotAvailable()} is fired if the {@link Task} isn't found.
     * @param taskId id of task to find.
     */
    @Override
    public void getTask(@NonNull final String taskId, @NonNull final GetTaskCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Task task = mTasksDao.getTaskById(taskId);

                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(task != null){
                            callback.onTaskLoaded(task);
                        }
                        else{
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void saveTask(@NonNull final Task task) {
        checkNotNull(task);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.insertTask(task);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void completeTask(@NonNull final Task task) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.updateCompleted(task.getId(), true);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull final Task task) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.updateCompleted(task.getId(), false);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public int clearCompletedTask() {
        final int[] deleteCount = new int[1];
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {

                deleteCount[0] = mTasksDao.deleteCompletedTask();

            }
        };

        mAppExecutors.getDiskIO().execute(deleteRunnable);
        return deleteCount[0];
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteAllTasks();
            }
        };

        mAppExecutors.getDiskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteTask(@NonNull final String taskId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mTasksDao.deleteTaskById(taskId);
            }
        };

        mAppExecutors.getDiskIO().execute(deleteRunnable);
    }
}
