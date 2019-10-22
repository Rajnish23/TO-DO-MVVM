package com.architecture.to_do_mvvm;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.data.source.TasksDataSource;
import com.architecture.to_do_mvvm.data.source.TasksRepository;
import com.google.common.base.Strings;

public abstract class TaskViewModel extends BaseObservable implements TasksDataSource.GetTaskCallback {

    public final ObservableField<String> snackBarText = new ObservableField<>();

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> description = new ObservableField<>();

    private final ObservableField<Task> mTaskObservable = new ObservableField<>();

    private final TasksRepository mTaskRepository;

    private final Context mContext;

    private boolean mIsDataLoading;

    public TaskViewModel(final TasksRepository mTaskRepository, final Context context) {
        this.mTaskRepository = mTaskRepository;
        this.mContext = context;

        mTaskObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Task task = mTaskObservable.get();
                if(task != null){
                    title.set(task.getTitle());
                    description.set(task.getDescription());
                }
                else {
                    title.set(mContext.getString(R.string.no_data));
                    description.set(mContext.getString(R.string.no_data_description));
                }
            }
        });
    }

    public void start(String taskId){
        if(!Strings.isNullOrEmpty(taskId)){
            mIsDataLoading = true;
            mTaskRepository.getTask(taskId, this);
        }
    }

    public void setTask(Task task){
        mTaskObservable.set(task);
    }

    public void setCompleted(boolean isComplete){
        if(mIsDataLoading ){
            return;
        }

        Task task = mTaskObservable.get();

        if (isComplete){
            mTaskRepository.completeTask(task);
            snackBarText.set(mContext.getResources().getString(R.string.task_marked_complete));
        }
        else{
            mTaskRepository.activateTask(task);
            snackBarText.set(mContext.getResources().getString(R.string.task_marked_active));
        }
    }

    public void deleteTask(){
        if(mTaskObservable.get() != null){
            mTaskRepository.deleteTask(mTaskObservable.get().getId());
        }
    }

    public void onRefresh(){
        if(mTaskObservable.get() != null){
            start(mTaskObservable.get().getId());
        }
    }

    public String getSnackBarText() {
        return snackBarText.get();
    }

    @Nullable
    protected String getTaskId(){
        return mTaskObservable.get().getId();
    }

    @Override
    public void onTaskLoaded(Task task) {
        mTaskObservable.set(task);
        mIsDataLoading = false;
        notifyChange(); // For the @Bindable Properties
    }

    @Override
    public void onDataNotAvailable() {
        mTaskObservable.set(null);
        mIsDataLoading = false;
    }

    /**
     * "completed" is two-way bound, so in order to intercept the new value, use {@link @Bindable}
     * @return
     */
    @Bindable
    public boolean getCompleted(){
        Task task = mTaskObservable.get();
        return task != null && task.isCompleted();
    }

    @Bindable
    public boolean isDataAvailable(){
        return mTaskObservable.get() != null;
    }

    @Bindable
    public boolean isDataLoading(){
        return mIsDataLoading;
    }


    @Bindable
    public String getTitleForList(){
        if(mTaskObservable.get() == null){
            return mContext.getString(R.string.no_data);
        }
        return mTaskObservable.get().getTitleForList();
    }
}
