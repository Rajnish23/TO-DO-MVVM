package com.architecture.to_do_mvvm.util;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Strings;

/**
 * Snackbar Utility call to show text
 */
public class SnackbarUtils {

    /**
     *
     * @param view to find the parent of view and get context
     * @param text to display
     */

    public static void showSnackBar(View view, String text){
        if(view == null || Strings.isNullOrEmpty(text)){
            return;
        }
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
