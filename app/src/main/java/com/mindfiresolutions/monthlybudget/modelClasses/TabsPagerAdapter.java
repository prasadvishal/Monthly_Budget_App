package com.mindfiresolutions.monthlybudget.modelClasses;

/**
 * Created by Vishal Prasad on 5/31/2017.
 * * modified on 5/31/2017
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String year;
    private String month;
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public Fragment getItem(int index) {
        Bundle bundle = new Bundle();
        switch (index) {
            case 0:

                ExpenseFragment expenseFragment = new ExpenseFragment();
                bundle.putString(MONTH_KEY,month);
                bundle.putString(YEAR_KEY,year);
                expenseFragment.setArguments(bundle);
                return expenseFragment;
            case 1:
                IncomeFragment incomeFragment = new IncomeFragment();
                bundle.putString(MONTH_KEY,month);
                bundle.putString(YEAR_KEY,year);
                incomeFragment.setArguments(bundle);
                return incomeFragment;

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}