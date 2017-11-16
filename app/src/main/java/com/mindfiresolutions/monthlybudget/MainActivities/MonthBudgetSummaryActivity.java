package com.mindfiresolutions.monthlybudget.MainActivities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.mindfiresolutions.monthlybudget.DatabaseFiles.DatabaseHandler;
import com.mindfiresolutions.monthlybudget.R;
import com.mindfiresolutions.monthlybudget.Utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;

/*
    Created By: Vishal on 06/01/2017
    Last Modified on 5/2/2017

    This file Displays the monthly Summary of the Budget and displays a Pie Chart.
 */
public class MonthBudgetSummaryActivity extends AppCompatActivity {
    private final String TAG = MonthBudgetSummaryActivity.class.getSimpleName();
    private String mMonth, mYear;
    private TextView mTotSaving;
    private TextView mLableTotSaving;
    private double mTotalIncome;
    private double mTotalExpense;
    private PieChart mPieChartBudgetSummary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_budget_summary);
        initView();
    }

    /**
     * Method to initialize Views and Displaying Total Expense, Total
     */
    private void initView() {
        ActionBar mActionBar = getSupportActionBar();
        Intent i = getIntent();
        DatabaseHandler mDatabaseHandler = DatabaseHandler.getInstance(MonthBudgetSummaryActivity.this);
        mPieChartBudgetSummary = (PieChart)findViewById(R.id.monthly_budget_piechart_budget_summary);
        if(i.hasExtra(MONTH_KEY) && i.hasExtra(YEAR_KEY))
        {
            mMonth = i.getExtras().get(MONTH_KEY).toString();
            mYear = i.getExtras().get(YEAR_KEY).toString();
        }
        if (mActionBar != null && mActionBar.isShowing()) {
            mActionBar.setHomeButtonEnabled(true);
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            //check for Actionbar if present then set tittle
            if (i.hasExtra(MONTH_KEY) && i.hasExtra(YEAR_KEY))
                mActionBar.setTitle(mMonth + " " + mYear);
            mActionBar.setSubtitle(R.string.prompt_budget_summary);
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView mTotExpense = (TextView) findViewById(R.id.monthly_budget_txt_tot_expense);
        TextView mTotIncome = (TextView) findViewById(R.id.monthly_budget_txt_tot_income);
        mTotSaving=(TextView)findViewById(R.id.monthly_budget_txt_tot_saving);
        mLableTotSaving=(TextView)findViewById(R.id.monthly_budget_txt_lable_tot_saving);
        mTotalIncome = mDatabaseHandler.getTotalMonthIncome(mMonth,mYear);
        mTotalExpense = mDatabaseHandler.getTotalMonthExpense(mMonth,mYear);
        mTotExpense.setText(String.valueOf(mTotalExpense));
        mTotIncome.setText(String.valueOf(mTotalIncome));
        double saving = (mTotalIncome)-(mTotalExpense);
        makePieChart(saving);

    }

    /**
     * This method makes Pie Chart on screen and also sets the lable if expense > income
     */
    private void makePieChart(double saving) {
        if(saving<0)
        {
            mLableTotSaving.setText(getString(R.string.prompt_total_over_expense));
            mLableTotSaving.setTextColor(Constants.RED);
            mTotSaving.setTextColor(Constants.RED);
            mTotSaving.setText(String.valueOf(mTotalExpense-mTotalIncome));
            int[] yValues = {(int)mTotalExpense, (int)(mTotalExpense-mTotalIncome)};
            String[] xValues = {getString(R.string.prompt_total_over_expense), getString(R.string.prompt_total_expense)};
            setDataForPieChart(mPieChartBudgetSummary,yValues,xValues);

        }
        else {
            mTotSaving.setText(String.valueOf(saving));
            int[] yValues = {(int)mTotalExpense, (int)saving};
            String[] xValues = {getString(R.string.prompt_total_saving), getString(R.string.prompt_total_expense)};
            setDataForPieChart(mPieChartBudgetSummary,yValues,xValues);
        }
    }

    /**
     * This function is to draw pie chart of Total Expense and Total Savings
     * @param view
     * @param yValues
     * @param xValues
     */
    private void setDataForPieChart(PieChart view,int yValues[], String[] xValues) {
        view.setRotationEnabled(true);
        List<PieEntry> yVals1 = new ArrayList<PieEntry>();
        final int[] MY_COLORS = {
                Color.rgb(255,18,3), Color.rgb(80,180,100)
        };
        for (int i = 0; i < yValues.length; i++)
            yVals1.add(new PieEntry(yValues[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (String xValue : xValues) xVals.add(xValue);

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData (dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        //data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);

        view.setData(data);

        // undo all highlights
        view.highlightValues(null);

        // refresh/update pie chart
        view.invalidate();

        // animate piechart
        view.animateXY(1400, 1400);


        // Legends to show on bottom of the graph
        Legend l = view.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                intent = new Intent(this, MonthEntriesActivity.class);
                intent.putExtra(MONTH_KEY, mMonth);
                intent.putExtra(YEAR_KEY, mYear);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
