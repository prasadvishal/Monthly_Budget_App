package com.mindfiresolutions.monthlybudget.modelClasses;

/**
 * Created by Vishal Prasad on 5/30/2017.
 * last modified on 5/31/2017
 */

public class MonthYear {
    private  String year;
    private String month;

    public MonthYear(String m, String yr){
        this.month=m;
        this.year=yr;
    }
    // Empty constructor
    public MonthYear(){}

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }



    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
