package com.pedrogonic.ditvapp;

import android.content.Context;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by pedrogonic on 6/10/15.
 */
public class Utils {
    public void hideKeyboard(Context context, Window window) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(window.getCurrentFocus().getWindowToken(), 0);
    }
}
