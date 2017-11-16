package com.mindfiresolutions.monthlybudget.Utilities.LoggerUtilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Vishal Prasad on 5/30/2017.
 * Last Modified on 5/30/2017
 */

public class LoggerUtility {

    public static void makeShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void makeLongToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void makeLog(String tag, String msg) {
        Log.e(tag, msg);
    }

}

