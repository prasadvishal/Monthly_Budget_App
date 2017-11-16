package com.mindfiresolutions.monthlybudget.RvAdapterMonths;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindfiresolutions.monthlybudget.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mindfiresolutions.monthlybudget.Utilities.Constants.MONTH_KEY;
import static com.mindfiresolutions.monthlybudget.Utilities.Constants.YEAR_KEY;


/**
 * Created by Vishal Prasad on 5/25/2017.
 * * modified on 5/25/2017
 */

public class RvAdapterMonthList extends RecyclerView.Adapter<RvAdapterMonthList.MyViewHolder> {
    public ArrayList<HashMap<String,String>> myValues;
    public RvAdapterMonthList(ArrayList<HashMap<String,String>> myValues){
        this.myValues= myValues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_montths_list, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.month.setText(myValues.get(position).get(MONTH_KEY));
        holder.year.setText(myValues.get(position).get(YEAR_KEY));

    }


    @Override
    public int getItemCount() {
        return myValues.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView month,year;

        public MyViewHolder(View itemView) {
            super(itemView);
            month = (TextView)itemView.findViewById(R.id.rv_item_edt_month);
            year = (TextView)itemView.findViewById(R.id.rv_item_edt_year);
        }
    }
}