package com.mindfiresolutions.monthlybudget.MainActivities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mindfiresolutions.monthlybudget.DatabaseFiles.DatabaseHandler;
import com.mindfiresolutions.monthlybudget.R;
import com.mindfiresolutions.monthlybudget.RvAdapterMonths.RecyclerItemClickListener;
import com.mindfiresolutions.monthlybudget.RvAdapterMonths.RvAdapterMonthList;
import com.mindfiresolutions.monthlybudget.Utilities.DateUtils.DateUtils;
import com.mindfiresolutions.monthlybudget.Utilities.LoggerUtilities.LoggerUtility;
import com.mindfiresolutions.monthlybudget.Utilities.TextUtils.TextUtilities;
import com.mindfiresolutions.monthlybudget.modelClasses.MonthYear;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;

/**
 * This Activity displays the list of the months whose entry has been made
 * and allows new entry.
 * <p>
 * <p>
 * Created by Vishal Prasad on 5/24/2017.
 * Last Modified on 5/30/2017
 */

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseHandler mDatabaseHandler;
    private ArrayList<HashMap<String, String>> mMonthsList;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAddNewMonthAlert();
            }
        });

        if (!mDatabaseHandler.isDatabaseEmpty())       //
        {
            readFromDb();
        }
    }

    /*
        method to initialize views and variables
     */
    private void initViews() {
        mDatabaseHandler = DatabaseHandler.getInstance(HomeActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_home_toolbar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.activity_home_fab);
        mMonthsList = new ArrayList<>();
    }

    /**
     * makeAddNewMonthAlert() pops up an Alert Box for adding new month entry into the database
     * with suggestion of currrent month.
     */
    private void makeAddNewMonthAlert() {
        mDatabaseHandler = DatabaseHandler.getInstance(HomeActivity.this);
        LayoutInflater li = LayoutInflater.from(HomeActivity.this);

        // only single use of Alert Box in this Application
        View promptsView = li.inflate(R.layout.alert_add_new_month, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);

        alertDialogBuilder.setView(promptsView);

        // reference UI elements from alert_add_new_month in similar fashion

        final AlertDialog alertDialog = alertDialogBuilder.create();
        final Spinner mSpnrMonth = (Spinner) promptsView.findViewById(R.id.spnr_alert_add_new_month);
        final Button mBtnAddMonth = (Button) promptsView.findViewById(R.id.btn_alert_add_new_month);
        final EditText mEdtYear = (EditText) promptsView.findViewById(R.id.edt_year_alert_add_new_month);
        mEdtYear.setText(DateUtils.getCurrentYear());
        mSpnrMonth.setOnItemSelectedListener(this);
        mSpnrMonth.setSelection(DateUtils.getCurrentMonth());
        mBtnAddMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpnrMonth.getSelectedItemPosition() == 0) {
                    Toast.makeText(HomeActivity.this, R.string.spinner_error_no_selection, Toast.LENGTH_LONG).show();
                    mSpnrMonth.performClick();
                    return;
                }
                if ((TextUtilities.getDoubleFromView(mEdtYear) < Integer.parseInt(DateUtils.getCurrentYear()))
                        || (TextUtilities.getDoubleFromView(mEdtYear) > Double.parseDouble(DateUtils.getCurrentYear()) + 10)) //from current to next 10 years
                {
                    TextUtilities.requestFocusIfError(mEdtYear, getResources().getString(R.string.error_invalid_year));
                    return;

                }
                if (!mDatabaseHandler.hasEntry(mSpnrMonth.getSelectedItem().toString(), mEdtYear.getText().toString())) {
                    mDatabaseHandler.addMonth(new MonthYear(mSpnrMonth.getSelectedItem().toString(), mEdtYear.getText().toString()));
                } else {
                    LoggerUtility.makeShortToast(HomeActivity.this, getString(R.string.propmt_entry_exists_in_db));

                }
                HomeActivity.this.finish();
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));

            }
        });

        // show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * This Method Reads month list from the database
     */
    private void readFromDb() {
        mMonthsList.clear();
        if(mDatabaseHandler.getMonthsList()!=null) {
            mMonthsList = mDatabaseHandler.getMonthsList();
            setMonthListAdapter(mMonthsList);
        }
        else
            LoggerUtility.makeShortToast(HomeActivity.this,getString(R.string.error_empty_database));
    }

    /**
     * this method sets the Recycler View Adapter of Month List in the Database, on HomeScreen
     * @param monthEntryList
     */
    private void setMonthListAdapter(final ArrayList<HashMap<String, String>> monthEntryList) {
        RvAdapterMonthList adapter = new RvAdapterMonthList(monthEntryList);
        RecyclerView myView =  (RecyclerView)findViewById(R.id.content_main_recyclerview);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(HomeActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

        myView.addOnItemTouchListener(
                new RecyclerItemClickListener(HomeActivity.this, myView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(HomeActivity.this, MonthEntriesActivity.class);
                        i.putExtra(MONTH_KEY, mMonthsList.get(position).get(MONTH_KEY));
                        i.putExtra(YEAR_KEY, mMonthsList.get(position).get(YEAR_KEY));
                        startActivity(i);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO: Overriden Method for Spinner selection in the alert box

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //TODO: Overriden Method for Spinner selection in the alert box
    }
}
