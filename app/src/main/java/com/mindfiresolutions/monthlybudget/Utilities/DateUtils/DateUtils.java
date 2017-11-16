package com.mindfiresolutions.monthlybudget.Utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_3_FORMAT;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_FORMAT;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_FORMAT;

/**
 * Created by Vishal Prasad on 5/31/2017.
 * Last Modified on 5/31/2017
 */

public class DateUtils {
    /*
      function to get current month in number
   */
     static public int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(MONTH_FORMAT);
        return Integer.parseInt(df.format(c.getTime()));
    }

    /*
        function to get current year in number
    */
   static public String getCurrentYear() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(YEAR_FORMAT);
        return (df.format(c.getTime()));
    }

    static public String getCurrentMonthInWords() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(MONTH_3_FORMAT);
        return (df.format(c.getTime()));
    }


}
