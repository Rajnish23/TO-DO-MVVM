package com.architecture.to_do_mvvm.ui.taskdetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;


import com.architecture.to_do_mvvm.Injection;
import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.ViewModelHolder;
import com.architecture.to_do_mvvm.ui.addedittask.AddEditTaskActivity;
import com.architecture.to_do_mvvm.ui.addedittask.AddEditTaskFragment;
import com.architecture.to_do_mvvm.util.ActivityUtils;

import static com.architecture.to_do_mvvm.ui.addedittask.AddEditTaskActivity.ADD_EDIT_RESULT_OK;
import static com.architecture.to_do_mvvm.ui.taskdetail.TaskDetailFragment.REQUEST_EDIT_TASK;

public class TaskDetailActivity extends AppCompatActivity implements TaskDetailNavigator {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    public static final String TASKDETAIL_VIEWMODEL_TAG = "TASKDETAIL_VIEWMODEL_TAG";

    public static final int DELETE_RESULT_OK = RESULT_FIRST_USER + 2;

    public static final int EDIT_RESULT_OK = RESULT_FIRST_USER + 3;

    private TaskDetailViewModel mTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        setupToolbar();

        TaskDetailFragment detailFragment = findOrCreateViewFragment();

        mTaskViewModel = findOrCreateViewModel();

        //set Navigator on viewmodel
        mTaskViewModel.setNavigator(this);

        //Link view with viewmodel
        detailFragment.setViewModel(mTaskViewModel);
    }

    @Override
    protected void onDestroy() {
        mTaskViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_EDIT_TASK){
            if(resultCode == ADD_EDIT_RESULT_OK){
                setResult(EDIT_RESULT_OK);
                finish();
            }
        }

        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @NonNull
    private TaskDetailViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<TaskDetailViewModel> retainedViewModel =
                (ViewModelHolder<TaskDetailViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(TASKDETAIL_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getmViewModel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getmViewModel();
        } else {
            // There is no ViewModel yet, create it.
            TaskDetailViewModel viewModel = new TaskDetailViewModel(
                    getApplicationContext(),
                    Injection.provideTaskRepository(getApplicationContext()));

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    TASKDETAIL_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @NonNull
    private TaskDetailFragment findOrCreateViewFragment() {
        // Get the requested task id
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskDetailFragment, R.id.contentFrame);
        }
        return taskDetailFragment;
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onTaskDeleted() {
        setResult(DELETE_RESULT_OK);
        //If the task was deleted successfully, go back to the list.
        finish();
    }

    @Override
    public void onStartEditTask() {
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }
}
