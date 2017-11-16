package com.mindfiresolutions.monthlybudget.Utilities.TextUtils;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Vishal Prasad on 5/30/2017.
 * Last Modified on 5/30/2017
 */

public class TextUtilities {
    public static Double getDoubleFromView(EditText view) {
        if((view.getText().toString().trim().length())==0)
            return 0.0;
        else
            return (Double.parseDouble(view.getText().toString().trim()));
    }

    public static void requestFocusIfError(EditText view, String errorMsg) {
        view.setError(errorMsg);
        view.requestFocus();
    }

    public static String getTextFromView(EditText view) {
        return (view.getText().toString().trim());
    }

    public static String getTextFromView(TextView view) {
        return (view.getText().toString().trim());
    }
}
