package com.mindfiresolutions.monthlybudget.RvAdapterBudgetOperations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindfiresolutions.monthlybudget.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.AMOUNT_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.DATE_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.TIME_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.TITLE_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.TYPE_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;


/**
 * Created by Vishal Prasad on 5/31/2017.
 * * modified on 5/31/2017
 */

public class RvAdapterBudgetOperationsList extends RecyclerView.Adapter<RvAdapterBudgetOperationsList.MyViewHolder> {
    public ArrayList<HashMap<String,String>> myValues;
    public RvAdapterBudgetOperationsList(ArrayList<HashMap<String,String>> myValues){
        this.myValues= myValues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_budget_operation, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.date.setText(myValues.get(position).get(DATE_KEY));
        holder.time.setText(myValues.get(position).get(TIME_KEY));
        holder.type.setText(myValues.get(position).get(TYPE_KEY));
        holder.title.setText(myValues.get(position).get(TITLE_KEY));
        holder.amount.setText(myValues.get(position).get(AMOUNT_KEY));

    }


    @Override
    public int getItemCount() {
        if(myValues==null)
            return 0;
        else
        return myValues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date,time,title,amount,type;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.rv_item_budget_opr_txt_date);
            time = (TextView)itemView.findViewById(R.id.rv_item_budget_opr_txt_time);
            title = (TextView)itemView.findViewById(R.id.rv_item_budget_opr_txt_title);
            amount = (TextView)itemView.findViewById(R.id.rv_item_budget_opr_txt_amount);
            type = (TextView)itemView.findViewById(R.id.rv_item_budget_opr_txt_type);

        }
    }
}