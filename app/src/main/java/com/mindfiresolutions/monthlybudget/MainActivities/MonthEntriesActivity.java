package com.mindfiresolutions.monthlybudget.MainActivities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mindfiresolutions.monthlybudget.R;
import com.mindfiresolutions.monthlybudget.modelClasses.TabsPagerAdapter;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;

/**
 * Created By: Vishal on 06/01/2017
 * last modified on 06/02/2017
 * this
 */

public class MonthEntriesActivity extends AppCompatActivity implements ActionBar.TabListener {
    private ViewPager viewPager;
    private ActionBar mActionBar;
    private String[] mBudgetOperationTypeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_entries);
        initView();
        // Adding Tabs
        for (String tab_name : mBudgetOperationTypeArray) {
            mActionBar.addTab(mActionBar.newTab().setText(tab_name).setTabListener(this));
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                mActionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    /**
     * method to initialize views
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        mActionBar = getSupportActionBar();
        Intent i = getIntent();
        TabsPagerAdapter mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mBudgetOperationTypeArray = getResources().getStringArray(R.array.budget_operation_type_array);
        if ((i.getExtras().get(MONTH_KEY) != null) && (i.getExtras().get(YEAR_KEY) != null))  {
            mAdapter.setMonth(i.getExtras().get(MONTH_KEY).toString());
            mAdapter.setYear(i.getExtras().get(YEAR_KEY).toString());
        }
        viewPager.setAdapter(mAdapter);
        //check for Actionbar if present then set tittle
        if (mActionBar != null && mActionBar.isShowing()) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            if ((i.getExtras().get(MONTH_KEY) != null) && (i.getExtras().get(YEAR_KEY) != null))
                mActionBar.setTitle(i.getExtras().get(MONTH_KEY).toString() + " " + i.getExtras().get(YEAR_KEY).toString());
            //mActionBar.setSubtitle(R.string.prompt_performance_summary);
            mActionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_budget_summary, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent, i;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.action_monthly_budget_summary:
                i = getIntent();
                intent = new Intent(MonthEntriesActivity.this, MonthBudgetSummaryActivity.class);
                intent.putExtra(MONTH_KEY, i.getExtras().get(MONTH_KEY).toString());
                intent.putExtra(YEAR_KEY, i.getExtras().get(YEAR_KEY).toString());
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
