package com.architecture.to_do_mvvm.ui.tasks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.architecture.to_do_mvvm.Injection;
import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.ViewModelHolder;
import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.ui.addedittask.AddEditTaskActivity;
import com.architecture.to_do_mvvm.util.ActivityUtils;
import com.google.android.material.navigation.NavigationView;

public class TaskActivity extends AppCompatActivity implements TaskItemNavigator, TasksNavigator {

    private DrawerLayout mDrawerLayout;
    private TasksViewModel mViewModel;
    public static final String TASKS_VIEWMODEL_TAG = "TASKS_VIEWMODEL_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        setupNavigationDrawer();

        TasksFragment tasksFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();
        mViewModel.setNavigator(this);

        //Link view and viewmodel
        tasksFragment.setViewmodel(mViewModel);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private TasksFragment findOrCreateViewFragment() {
        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(tasksFragment ==  null){
            //Create new fragment instance
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), tasksFragment, R.id.contentFrame);
        }

        return tasksFragment;

    }

    private TasksViewModel findOrCreateViewModel(){
        @SuppressWarnings("unchecked")
        ViewModelHolder<TasksViewModel> retainedViewModel = (ViewModelHolder<TasksViewModel>) getSupportFragmentManager()
                .findFragmentByTag(TASKS_VIEWMODEL_TAG);

        if(retainedViewModel != null && retainedViewModel.getmViewModel() != null){
            return retainedViewModel.getmViewModel();
        }
        else{
            //No ViewModel create a new one
            TasksViewModel viewModel = new TasksViewModel(
                    Injection.provideTaskRepository(getApplicationContext())
                    , getApplicationContext());

            //and bind it to this activity's lifecycle using fragment manager
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    TASKS_VIEWMODEL_TAG);
            return viewModel;
        }


    }

    private void setupNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if(navigationView != null){
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.list_navigation_menu_items:
                        //Do Nothing, We're already on that screen
                        break;

                    case R.id.statistics_navigation_menu_item:
                        //TODO Navigate to Statistics screen

                        break;
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void openTaskDetails(String taskId) {
        // TODO: 10/21/2019   open task details screen
    }

    @Override
    public void addNewTask() {
        Intent addIntent = new Intent(TaskActivity.this, AddEditTaskActivity.class);
        startActivityForResult(addIntent, AddEditTaskActivity.REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.handleActivityResult(requestCode, resultCode);
    }

    @Override
    public void completeTask(Task task) {
        mViewModel.completeTask(task);
    }

    @Override
    public void activateTask(Task task) {
        mViewModel.activateTask(task);
    }
}
