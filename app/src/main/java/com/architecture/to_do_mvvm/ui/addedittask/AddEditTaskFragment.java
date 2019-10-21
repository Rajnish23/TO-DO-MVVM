package com.architecture.to_do_mvvm.ui.addedittask;


import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.architecture.to_do_mvvm.R;
import com.architecture.to_do_mvvm.databinding.FragmentAddEditTaskBinding;
import com.architecture.to_do_mvvm.util.SnackbarUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Main UI for the add Task Screen. User can input title and description
 */
public class AddEditTaskFragment extends Fragment {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private AddEditTaskViewModel mViewModel;

    private FragmentAddEditTaskBinding mBinding;

    private Observable.OnPropertyChangedCallback mSnackbarCallback;

    public AddEditTaskFragment() {
        // Required empty public constructor
    }
    public static AddEditTaskFragment newInstance(){
        return new AddEditTaskFragment();
    }

    public void setViewmodel(@NonNull AddEditTaskViewModel viewmodel){
        mViewModel = viewmodel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(mBinding == null){
            mBinding = FragmentAddEditTaskBinding.inflate(inflater, container,false);
        }

        mBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(true);
        setRetainInstance(true);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getArguments() != null){
            mViewModel.start(getArguments().getString(ARGUMENT_EDIT_TASK_ID));
        }
        else{
            mViewModel.start(null );
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupFab();

        setupSnackBar();

        setupActionBar();
    }

    @Override
    public void onDestroy() {
        if(mSnackbarCallback != null){
            mViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
        }
        super.onDestroy();
    }

    private void setupSnackBar(){
        mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                SnackbarUtils.showSnackBar(getView(), mViewModel.getSnackbarText());
            }
        };

        mViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
    }

    private void setupFab(){
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.saveTask();
            }
        });
    }

    private void setupActionBar(){
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getActionBar();

        if(actionBar == null){
            return;
        }

        if(getArguments().getString(ARGUMENT_EDIT_TASK_ID) != null){
            actionBar.setTitle(R.string.edit_task);
        }
        else{
            actionBar.setTitle(R.string.add_task);
        }
    }
}
