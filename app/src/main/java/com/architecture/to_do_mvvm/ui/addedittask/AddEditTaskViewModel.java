package com.architecture.to_do_mvvm.ui.addedittask;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.data.source.TasksDataSource;
import com.architecture.to_do_mvvm.data.source.TasksRepository;

public class AddEditTaskViewModel implements TasksDataSource.GetTaskCallback {

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> description = new ObservableField<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> snackbarText = new ObservableField<>();

    private final TasksRepository mTasksRepository;

    private final Context mContext; // To avoid leaks, this must be applicationContext.

    @Nullable
    private String mTaskId;

    private boolean mIsNewTask;

    private boolean mIsDataLoaded = false;

    private AddEditTaskNavigator mEditTaskNavigator;

    public AddEditTaskViewModel(TasksRepository mTasksRepository, Context mContext) {
        this.mTasksRepository = mTasksRepository;
        this.mContext = mContext;
    }

    void onActivityCreated(AddEditTaskNavigator addEditTaskNavigator){
        mEditTaskNavigator = addEditTaskNavigator;
    }

    void onActivityDestroyed(){
        //Clear reference to avoid memory leaks
        mEditTaskNavigator = null;
    }

    public void start(String taskId){
        if(dataLoading.get()){
            //Already loading ignore
            return;
        }

        mTaskId = taskId;
        if(mTaskId == null){
            //No need to populate, it's a new task
            mIsNewTask = true;
            return;
        }
        if(mIsDataLoaded){
            //No need to populate, already have data
            return;
        }

        mIsNewTask = false;
        dataLoading.set(true);

        mTasksRepository.getTask(taskId, this);
    }

    @Override
    public void onTaskLoaded(Task task) {
        //Set the title and description of the task.
        title.set(task.getTitle());
        description.set(task.getDescription());
        dataLoading.set(false);
        mIsDataLoaded = true;
    }

    @Override
    public void onDataNotAvailable() {
        dataLoading.set(false);
    }

    @Nullable
    public String getSnackbarText(){
        return snackbarText.get();
    }

    public boolean isNewTask(){
        return mIsNewTask;
    }

    public void saveTask(){
        if(isNewTask()){
            createTask(title.get(), description.get());
        }
        else{
            updateTask(title.get(), description.get());
        }
    }

    private void createTask(String title, String description){
        Task newTask = new Task(title, description);
        if(newTask.isEmpty()){
            snackbarText.set(mContext.getString(R.string.empty_task_message));
        }
        else{
            mTasksRepository.saveTask(newTask);
            navigateOnTaskSaved();
        }
    }

    private void updateTask(String title, String description){
        if(isNewTask()){
            throw new RuntimeException("Update task was called, but task is new");
        }

        mTasksRepository.saveTask(new Task(title, description, mTaskId));
        navigateOnTaskSaved();
    }

    private void navigateOnTaskSaved(){
        if(mEditTaskNavigator != null){
            mEditTaskNavigator.onTaskSaved();
        }
    }
}
