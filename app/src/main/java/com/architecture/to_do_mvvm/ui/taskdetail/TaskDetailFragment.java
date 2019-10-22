package com.architecture.to_do_mvvm.ui.taskdetail;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.databinding.FragmentTaskDetailBinding;
import com.architecture.to_do_mvvm.util.SnackbarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

    public static final String ARGUMENT_TASK_ID = "TASK_ID";

    public static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailViewModel mViewmodel;

    private Observable.OnPropertyChangedCallback mSnackbarCallback;

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    public static TaskDetailFragment newInstance(String taskId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_TASK_ID, taskId);
        TaskDetailFragment detailFragment = new TaskDetailFragment();
        detailFragment.setArguments(arguments);
        return detailFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentTaskDetailBinding mBinding = FragmentTaskDetailBinding.inflate(inflater,
                container, false);

        mBinding.setViewmodel(mViewmodel);

        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewmodel.start(getArguments().getString(ARGUMENT_TASK_ID));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFab();

        setupSnackBar();
    }

    @Override
    public void onDestroy() {
        if(mSnackbarCallback != null){
            mViewmodel.snackBarText.removeOnPropertyChangedCallback(mSnackbarCallback);
        }
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_delete:
                mViewmodel.deleteTask();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setViewModel(TaskDetailViewModel viewModel) {
        mViewmodel = viewModel;
    }

    private void setupFab() {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewmodel.startEditTask();
            }
        });
    }

    private void setupSnackBar() {
        mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                SnackbarUtils.showSnackBar(getView(), mViewmodel.getSnackBarText());
            }
        };

        mViewmodel.snackBarText.addOnPropertyChangedCallback(mSnackbarCallback);
    }

}
