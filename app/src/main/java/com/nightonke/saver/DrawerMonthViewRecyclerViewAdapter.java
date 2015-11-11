package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by 伟平 on 2015/10/20.
 */

public class DrawerMonthViewRecyclerViewAdapter
        extends RecyclerView.Adapter<DrawerMonthViewRecyclerViewAdapter.viewHolder> {

    private ArrayList<Double> expenses;
    private ArrayList<Integer> months;
    private ArrayList<Integer> years;

    private Context mContext;

    OnItemClickListener onItemClickListener;

    public DrawerMonthViewRecyclerViewAdapter(Context context) {
        mContext = context;
        expenses = new ArrayList<>();
        months = new ArrayList<>();
        years = new ArrayList<>();

        if (RecordManager.RECORDS.size() != 0) {

            int currentYear = RecordManager.RECORDS.
                    get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.YEAR);
            int currentMonth = RecordManager.RECORDS.
                    get(RecordManager.RECORDS.size() - 1).getCalendar().get(Calendar.MONTH);
            double currentMonthSum = 0;

            for (int i = RecordManager.RECORDS.size() - 1; i >= 0; i--) {
                if (RecordManager.RECORDS.get(i).getCalendar().get(Calendar.YEAR) == currentYear
                        && RecordManager.RECORDS.get(i).
                        getCalendar().get(Calendar.MONTH) == currentMonth) {
                    currentMonthSum += RecordManager.RECORDS.get(i).getMoney();
                } else {
                    expenses.add(currentMonthSum);
                    years.add(currentYear);
                    months.add(currentMonth);
                    currentMonthSum = 0;
                    currentYear = RecordManager.RECORDS.get(i).getCalendar().get(Calendar.YEAR);
                    currentMonth = RecordManager.RECORDS.get(i).getCalendar().get(Calendar.MONTH);
                }
            }
            expenses.add(currentMonthSum);
            years.add(currentYear);
            months.add(currentMonth);

        }


    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    @Override
    public DrawerMonthViewRecyclerViewAdapter.viewHolder
        onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.draw_month_list_view_item, parent, false);
        return new viewHolder(view) {
        };

    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {

        holder.month.setText(Utils.GetMonthShort(months.get(position) + 1));
        holder.month.setTypeface(Utils.GetTypeface());

        holder.year.setText(years.get(position) + "");
        holder.year.setTypeface(Utils.GetTypeface());

        if ("zh".equals(Utils.GetLanguage()))
            holder.money.setText("¥" + (int) (double) (expenses.get(position)));
        else
            holder.money.setText("$" + (int)(double)(expenses.get(position)));
        holder.money.setTypeface(Utils.GetTypeface());

    }

    public class viewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        @Optional
        @InjectView(R.id.month)
        TextView month;
        @Optional
        @InjectView(R.id.year)
        TextView year;
        @Optional
        @InjectView(R.id.money)
        TextView money;

        viewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.onItemClickListener = mItemClickListener;
    }

}
