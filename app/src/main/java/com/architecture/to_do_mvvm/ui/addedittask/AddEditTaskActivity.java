package com.architecture.to_do_mvvm.ui.addedittask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.architecture.to_do_mvvm.Injection;
import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.ViewModelHolder;
import com.architecture.to_do_mvvm.util.ActivityUtils;

public class AddEditTaskActivity extends AppCompatActivity implements AddEditTaskNavigator {

    public static final int REQUEST_CODE = 1;

    public static final int ADD_EDIT_RESULT_OK = RESULT_FIRST_USER + 1;

    public static final String ADD_EDIT_VIEWMODEL_TAG = "ADD_EDIT_VIEWMODEL_TAG";

    private AddEditTaskViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        AddEditTaskFragment addEditTaskFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();

        //Link view with viewmodel
        addEditTaskFragment.setViewmodel(mViewModel);

        //Set the navigator
        mViewModel.onActivityCreated(this);

    }

    @Override
    public void onTaskSaved() {
        setResult(ADD_EDIT_RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onActivityDestroyed();
    }

    @NonNull
    private AddEditTaskFragment findOrCreateViewFragment(){
        //View Fragment
        AddEditTaskFragment addEditTaskFragment = (AddEditTaskFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if(addEditTaskFragment == null){
            //Create a new fragment
            addEditTaskFragment = AddEditTaskFragment.newInstance();

            //Send the taskId to the fragment
            Bundle bundle = new Bundle();
            bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID,
                    getIntent().getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID));
            addEditTaskFragment.setArguments(bundle);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }
        return addEditTaskFragment;
    }

    private AddEditTaskViewModel findOrCreateViewModel(){
        @SuppressWarnings("unchecked")
        ViewModelHolder<AddEditTaskViewModel> retainedViewModel = (ViewModelHolder<AddEditTaskViewModel>) getSupportFragmentManager()
                .findFragmentByTag(ADD_EDIT_VIEWMODEL_TAG);

        if(retainedViewModel != null && retainedViewModel.getmViewModel() != null){
            return retainedViewModel.getmViewModel();
        }
        else{
            //Create a new viewmodel
            AddEditTaskViewModel viewModel = new AddEditTaskViewModel(
                    Injection.provideTaskRepository(getApplicationContext()),
                            getApplicationContext());
            //Bind it to activity lifecycle
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    ADD_EDIT_VIEWMODEL_TAG);

            return viewModel;
        }
    }
}
