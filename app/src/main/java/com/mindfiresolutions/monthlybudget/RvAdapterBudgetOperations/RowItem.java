package com.mindfiresolutions.monthlybudget.RvAdapterBudgetOperations;

/**
 * Created by Vishal Prasad on 5/25/2017.
 * * modified on 5/25/2017
 */

public class RowItem {

    private String Title;

    public RowItem(String Title){

        this.Title = Title;
    }

    public String getTitle(){

        return Title;
    }

    public void setTitle(String Title){

        this.Title = Title;
    }


    @Override
    public String toString() {
        return Title ;
    }
}