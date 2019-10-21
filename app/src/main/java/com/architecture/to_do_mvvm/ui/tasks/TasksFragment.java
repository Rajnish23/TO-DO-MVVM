package com.architecture.to_do_mvvm.ui.tasks;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.architecture.to_do_mvvm.Injection;
import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.data.Task;
import com.architecture.to_do_mvvm.data.source.TasksRepository;
import com.architecture.to_do_mvvm.databinding.FragmentTasksBinding;
import com.architecture.to_do_mvvm.databinding.TaskItemBinding;
import com.architecture.to_do_mvvm.util.ScrollChildSwipeRefreshLayout;
import com.architecture.to_do_mvvm.util.SnackbarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksFragment extends Fragment {

    private TasksViewModel mTasksViewModel;

    private FragmentTasksBinding mTasksFragBinding;

    private TasksAdapter mListAdapter;

    private Observable.OnPropertyChangedCallback mSnackbarCallback;

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTasksViewModel.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mTasksFragBinding = FragmentTasksBinding.inflate(inflater, container, false);
        mTasksFragBinding.setView(this);
        mTasksFragBinding.setViewmodel(mTasksViewModel);
        setHasOptionsMenu(true);

        return mTasksFragBinding.getRoot();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mTasksViewModel.clearCompletedTasks();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                mTasksViewModel.loadTasks(true);
                break;
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupSnackbar();

        setupFab();

        setupListAdpater();

        setupRefreshLayout();
    }

    @Override
    public void onDestroy() {
        mListAdapter.onDestroy();
        if (mSnackbarCallback != null) {
            mTasksViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
        }
        super.onDestroy();
    }

    private void setupSnackbar() {
        mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                SnackbarUtils.showSnackBar(getView(), mTasksViewModel.getSnackbarText());
            }
        };

        mTasksViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
    }

    private void showFilteringPopUpMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popupMenu.getMenuInflater().inflate(R.menu.filter_tasks, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
                        mTasksViewModel.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        mTasksViewModel.setFiltering(TasksFilterType.COMPLETED_TASKS);
                        break;
                    default:
                        mTasksViewModel.setFiltering(TasksFilterType.ALL_TASKS);
                        break;
                }
                mTasksViewModel.loadTasks(false);
                return true;
            }
        });

        popupMenu.show();
    }

    private void setupFab(){
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTasksViewModel.addNewTask();
            }
        });
    }

    private void setupListAdpater(){
        ListView listView = mTasksFragBinding.tasksList;
        mListAdapter = new TasksAdapter(new ArrayList<Task>(0),
                (TaskActivity) getActivity(), Injection.provideTaskRepository(getContext().getApplicationContext()),
                mTasksViewModel);

        listView.setAdapter(mListAdapter);
    }

    private void setupRefreshLayout(){
        ListView listView = mTasksFragBinding.tasksList;
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = mTasksFragBinding.refreshLayout;

        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        //Set the scrolling view in the custom swipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(listView);
    }

    public void setViewmodel(TasksViewModel viewmodel) {
        mTasksViewModel = viewmodel;
    }

    public static class TasksAdapter extends BaseAdapter {

        @Nullable
        private TaskItemNavigator mTaskItemNavigator;

        private final TasksViewModel mTasksViewModel;

        private List<Task> mTasks;

        private TasksRepository mTaskRepository;

        public TasksAdapter(List<Task> tasks, TaskActivity taskItemNavigator,
                            TasksRepository tasksRepository, TasksViewModel tasksViewModel) {
            mTaskItemNavigator = taskItemNavigator;
            mTaskRepository = tasksRepository;
            mTasksViewModel = tasksViewModel;
            setList(tasks);
        }

        @Override
        public int getCount() {
            return mTasks != null ? mTasks.size() : 0;
        }

        @Override
        public Task getItem(int i) {
            return mTasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            final Task task = getItem(position);
            TaskItemBinding mBinding;

            if (view == null) {
                //Inflate
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

                //Create the binding.
                mBinding = TaskItemBinding.inflate(inflater, viewGroup, false);
            } else {
                //Recycling View
                mBinding = DataBindingUtil.getBinding(view);
            }

            final TaskItemViewModel viewModel = new TaskItemViewModel(
                    viewGroup.getContext(), mTaskRepository
            );

            viewModel.setNavigator(mTaskItemNavigator);

            mBinding.setViewmodel(viewModel);

            viewModel.snackBarText.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    mTasksViewModel.snackbarText.set(viewModel.getSnackBarText());
                }
            });

            viewModel.setTask(task);

            //Perform click listener on complete checkbox and toggle the action.
            mBinding.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!task.isCompleted()){
                        viewModel.onCheck_CompleteTask(task);
                    }
                    else{
                        viewModel.onUnCheck_ActivateTask(task);
                    }
                }
            });

            return mBinding.getRoot();
        }

        public void onDestroy() {
            mTaskItemNavigator = null;
        }

        public void replaceData(List<Task> tasks) {
            setList(tasks);
        }

        private void setList(List<Task> tasks) {
            mTasks = tasks;
            notifyDataSetChanged();
        }
    }

}
