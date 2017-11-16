package com.mindfiresolutions.monthlybudget.modelClasses;

/**
 * Created by Vishal Prasad on 5/31/2017.
 * last modified on 5/31/2017
 */

public class MonthEntry {
    private String date;
    private String month;
    private String time;
    private String year;
    private String type;
    private String title;
    private String amount;


    public MonthEntry() {
    }
    public MonthEntry(String date,String month,String yr, String time,String type,String title,String amount)
    {
        this.date=date;
        this.month=month;
        this.year=yr;
        this.time=time;
        this.type=type;
        this.title=title;
        this.amount=amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

        public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

        public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

           public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
