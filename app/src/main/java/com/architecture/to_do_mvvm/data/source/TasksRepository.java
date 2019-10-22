package com.architecture.to_do_mvvm.data.source;

import androidx.annotation.NonNull;

import com.architecture.to_do_mvvm.data.Task;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

public class TasksRepository implements TasksDataSource{

    private static TasksRepository INSTANCE = null;

    private final TasksDataSource mTasksLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests
     */
    Map<String, Task> mCachedTasks;

    private TasksRepository(@NonNull TasksDataSource mTasksLocalDataSource) {
        this.mTasksLocalDataSource = mTasksLocalDataSource;
    }

    /**
     * Returns the Single instance of this class, creating if necessary
     *
     * @param mLocalDataSource the in device data source
     * @return the {@link TasksRepository} instance
     */
    public static TasksRepository getInstance(TasksDataSource mLocalDataSource){
        if(INSTANCE == null){
            INSTANCE = new TasksRepository(mLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(TasksDataSource)}
     * To create a new instance next time it's called
     */
    public static void destroyInstance(){
        INSTANCE = null;
    }

    @Override
    public void getTasks(@NonNull final LoadTasksCallback callback) {
        checkNotNull(callback);

        mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                callback.onTasksLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull final GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        mTasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);

        mTasksLocalDataSource.saveTask(task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        mTasksLocalDataSource.completeTask(task);
    }

    @Override
    public void completeTask(@NonNull final String taskId) {
        checkNotNull(taskId);

        mTasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                completeTask(task);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);

        mTasksLocalDataSource.activateTask(task);
    }

    @Override
    public void activateTask(@NonNull final String taskId) {
        checkNotNull(taskId);

        mTasksLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                activateTask(taskId);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public int clearCompletedTask() {
       return mTasksLocalDataSource.clearCompletedTask();
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        mTasksLocalDataSource.deleteTask(checkNotNull(taskId));
    }
}
