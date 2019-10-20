package com.architecture.to_do_mvvm.ui.tasks;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.data.source.TasksDataSource;
import com.architecture.to_do_mvvm.data.source.TasksRepository;

import java.util.ArrayList;
import java.util.List;

public class TasksViewModel extends BaseObservable {

    //These Observable Fields will update Views automatically
    final ObservableField<String> snackbarText = new ObservableField<>();

    public final ObservableList<Task> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableField<String> currentFilteringLabel = new ObservableField<>();

    public final ObservableField<Drawable> noTaskIconRes = new ObservableField<>();

    public final ObservableField<String> noTasksLabel = new ObservableField<>();

    public final ObservableBoolean tasksAddViewVisible = new ObservableBoolean();

    private TasksFilterType mCurrentFiltering = TasksFilterType.ALL_TASKS;

    private final TasksRepository mTasksRepository;

    private final ObservableBoolean mIsDataLoadingError = new ObservableBoolean(false);

    private Context mContext;

    private TasksNavigator mNavigator;

    public TasksViewModel(TasksRepository tasksRepository,
                          Context context) {
        mContext = context;
        mTasksRepository = tasksRepository;

        //Set Initial State
        setFiltering(TasksFilterType.ALL_TASKS);
    }

    public void setFiltering(TasksFilterType requestType) {
        mCurrentFiltering = requestType;

        //Depending on Filter type, set the filtering label, icon drawables etc.
        switch (requestType) {
            case ACTIVE_TASKS:
                currentFilteringLabel.set(mContext.getString(R.string.label_active));
                noTasksLabel.set(mContext.getString(R.string.no_tasks_active));
                noTaskIconRes.set(mContext.getResources().getDrawable(R.drawable.ic_check_circle_24dp));
                tasksAddViewVisible.set(false);
                break;
            case COMPLETED_TASKS:
                currentFilteringLabel.set(mContext.getString(R.string.label_completed));
                noTasksLabel.set(mContext.getString(R.string.no_tasks_completed));
                noTaskIconRes.set(mContext.getResources().getDrawable(R.drawable.ic_verified_user_24dp));
                tasksAddViewVisible.set(false);
                break;
            case ALL_TASKS:
                currentFilteringLabel.set(mContext.getString(R.string.label_all));
                noTasksLabel.set(mContext.getString(R.string.no_tasks_all));
                noTaskIconRes.set(mContext.getResources().getDrawable(R.drawable.ic_assignment_turned_in_24dp));
                tasksAddViewVisible.set(true);
                break;
        }
    }

    void setNavigator(TasksNavigator navigator) {
        mNavigator = navigator;
    }

    void onActivityDestroyed() {
        //Clear references to avoid memory leaks.
        mNavigator = null;
    }

    public String getSnackbarText() {
        return snackbarText.get();
    }

    public void start() {
        loadTasks(false);
    }

    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate, true);
    }

    public void addNewTask(){
        if(mNavigator != null){
            mNavigator.addNewTask();
        }
    }

    public void clearCompletedTasks(){
        mTasksRepository.clearCompletedTask();
        snackbarText.set(mContext.getString(R.string.completed_tasks_cleared));
        loadTasks(false, false);
    }

    void handleActivityResult(int requestCode, int resultCode){
        //TODO handle activity result
        if(true){
            switch (resultCode){

            }
        }
    }

    /**
     *
     * @param forceUpdate pass in true to refresh the data in the {@link TasksDataSource}
     * @param showLoadingUI pass in true to display the loading icon in the UI
     */
    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }

        if (forceUpdate) {
            mTasksRepository.refreshTasks();
        }

        mTasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> tasksToShow = new ArrayList<>();

                for (Task task : tasks) {
                    switch (mCurrentFiltering) {
                        case ALL_TASKS:
                            tasksToShow.add(task);
                            break;
                        case COMPLETED_TASKS:
                            if (task.isCompleted()) {
                                tasksToShow.add(task);
                            }
                            break;
                        case ACTIVE_TASKS:
                            if (task.isActive()) {
                                tasksToShow.add(task);
                            }
                            break;
                        default:
                            tasksToShow.add(task);
                            break;
                    }
                }

                if(showLoadingUI){
                    dataLoading.set(false);
                }

                mIsDataLoadingError.set(false);
                items.clear();
                items.addAll(tasksToShow);
                notifyPropertyChanged(com.architecture.to_do_mvvm.BR.empty);
            }

            @Override
            public void onDataNotAvailable() {
                mIsDataLoadingError.set(true);
            }
        });
    }

    @Bindable
    public boolean isEmpty() {
        return items.isEmpty();
    }

}
