package com.architecture.to_do_mvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Non-UI Fragment used to retain viewmodel
 * @param <VM>
 */
public class ViewModelHolder<VM> extends Fragment {

    private VM mViewModel;

    public ViewModelHolder() {
    }

    public static <M> ViewModelHolder createContainer(@NonNull M viewmodel){
        ViewModelHolder<M> viewModelHolder = new ViewModelHolder<>();
        viewModelHolder.setmViewModel(viewmodel);
        return viewModelHolder;
    }

    @Nullable
    public VM getmViewModel() {
        return mViewModel;
    }

    public void setmViewModel(@NonNull VM mViewModel) {
        this.mViewModel = mViewModel;
    }
}
