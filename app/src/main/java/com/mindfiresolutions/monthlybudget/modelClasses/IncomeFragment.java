package com.mindfiresolutions.monthlybudget.modelClasses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mindfiresolutions.monthlybudget.DatabaseFiles.DatabaseHandler;
import com.mindfiresolutions.monthlybudget.MainActivities.MonthEntriesActivity;
import com.mindfiresolutions.monthlybudget.R;
import com.mindfiresolutions.monthlybudget.RvAdapterBudgetOperations.RvAdapterBudgetOperationsList;
import com.mindfiresolutions.monthlybudget.RvAdapterMonths.RvAdapterMonthList;
import com.mindfiresolutions.monthlybudget.Utilities.Constants;
import com.mindfiresolutions.monthlybudget.Utilities.LoggerUtilities.LoggerUtility;
import com.mindfiresolutions.monthlybudget.Utilities.TextUtils.TextUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;

/**
 * Created by Vishal Prasad on 5/31/2017.
 */
public class IncomeFragment extends Fragment {

        private View rootView;
        private EditText mEdtExpenseTitle,mEdtExpenseAmount;
        private String mMonth,mYear,mType, mDateTime;
        private DatabaseHandler mDatabaseHandler;
        private Button mAddButton;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.budget_operation_fragment, container, false);
            initView();
            mMonth = getArguments().getString(MONTH_KEY);
            mYear = getArguments().getString(YEAR_KEY);
            mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(validInputs())
                    {
                        insertIntoDatabase();
                        Intent i = new Intent(getContext(), MonthEntriesActivity.class);
                        i.putExtra(MONTH_KEY, mMonth);
                        i.putExtra(YEAR_KEY, mYear);
                        getActivity().finish();
                        startActivity(i);
                        LoggerUtility.makeShortToast(getContext(),getString(R.string.prompt_successfully_added));
                    }
                }
            });

            final DatabaseHandler db = DatabaseHandler.getInstance(getContext());
            if(db.getMonthEntryList()!=null){
                ArrayList<HashMap<String, String>> monthEntryList = db.getMonthIncomeEntryList(mMonth,mYear);
                setListAdapter(monthEntryList);
            }
            else
                LoggerUtility.makeLongToast(getContext(),getString(R.string.error_empty_database));

            return rootView;
        }


    /**
     * This method initialize the views
     */
    private void initView() {
        mEdtExpenseTitle = (EditText) rootView.findViewById(R.id.budget_operation_fragment_edt_title);
        mEdtExpenseAmount = (EditText) rootView.findViewById(R.id.budget_operation_fragment_edt_amount);
        mDatabaseHandler = DatabaseHandler.getInstance(getContext());
        mType = Constants.INCOME;
        mDateTime = mDatabaseHandler.getCurrentDateTime();
        mAddButton=(Button)rootView.findViewById(R.id.budget_operation_fragment_btn_add);
        mAddButton.setText(getString(R.string.add_income_btn_text));
    }

    /**
     * Method to insert record into MonthEntryTable
     */
    private void insertIntoDatabase() {

        MonthEntry entry = new MonthEntry(mDateTime.substring(0,11),mMonth,mYear,mDateTime.substring(11),
                mType,mEdtExpenseTitle.getText().toString(),mEdtExpenseAmount.getText().toString());
        mDatabaseHandler.addMonthEntry(entry);
    }

    /**
     * This method sets the Reecycler View for Income list
     * @param IncomeList
     */
    private void setListAdapter(final ArrayList<HashMap<String, String>> IncomeList) {
        RvAdapterBudgetOperationsList adapter = new RvAdapterBudgetOperationsList(IncomeList);
        RecyclerView myView = (RecyclerView) rootView.findViewById(R.id.budget_operation_fragment_recyclerview);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);
    }

    /**
     * this method check if the data is valid for insertion into the database or not
     */
    private boolean validInputs() {

        if(TextUtilities.getTextFromView(mEdtExpenseTitle).length()==0)
        {
            TextUtilities.requestFocusIfError(mEdtExpenseTitle,getString(R.string.error_prompt_required));
            return false;
        }
        if(TextUtilities.getTextFromView(mEdtExpenseTitle).length()>35)
        {
            TextUtilities.requestFocusIfError(mEdtExpenseTitle,getString(R.string.error_prompt_title_too_long));
            return false;
        }
        if(TextUtilities.getTextFromView(mEdtExpenseAmount).length()==0)
        {
            TextUtilities.requestFocusIfError(mEdtExpenseAmount,getString(R.string.error_prompt_required));
            return false;
        }
        if(TextUtilities.getDoubleFromView(mEdtExpenseAmount)>=(double) getResources().getInteger(R.integer.max_amount_input))
        {
            TextUtilities.requestFocusIfError(mEdtExpenseAmount,getString(R.string.prompt_amount_too_big));
            LoggerUtility.makeShortToast(getContext(),getString(R.string.prompt_big_amount_insertion_suggestion));
            return false;
        }
        return true;
    }

}

