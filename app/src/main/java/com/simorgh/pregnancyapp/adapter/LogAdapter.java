package com.simorgh.pregnancyapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.persianmaterialdatepicker.date.DatePickerDialog;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.View.main.LogsFragmentDirections;
import com.simorgh.timelineview.TimeLineView;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class LogAdapter extends ListAdapter<LogItem, LogAdapter.LogItemViewHolder> {
    private Calendar temp = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private volatile PersianCalendar tempPersianDate = CalendarTool.GregorianToPersian(Calendar.getInstance());
    private Calendar min = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar max = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    private Calendar now = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

    public LogAdapter(@NonNull DiffUtil.ItemCallback<LogItem> diffCallback) {
        super(diffCallback);
    }

    protected LogAdapter(@NonNull AsyncDifferConfig<LogItem> config) {
        super(config);
    }

    @NonNull
    @Override
    public LogItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new LogItemViewHolder(v);
    }

    private PersianCalendar persianCalendar = new PersianCalendar();

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull LogItemViewHolder holder, final int position) {
        LogItem item = getItem(position);
        if (item != null) {
            TimeLineView timeLineView = holder.itemView.findViewById(R.id.time_line_view);
            TextView dayNumber = holder.itemView.findViewById(R.id.tv_day_number);
            ImageView babyImage = holder.itemView.findViewById(R.id.img_baby);
            Button showLogs = holder.itemView.findViewById(R.id.btn_show_logs);

            persianCalendar = CalendarTool.GregorianToPersian(item.getDate().getCalendar());
            timeLineView.setText(persianCalendar.getPersianDay() + " " + persianCalendar.getPersianMonthName());
            dayNumber.setText(String.format("روز %d بارداری", item.getDaysFromStart()));


            initShowLogClickListener(item, showLogs);

            initTimeLineClickListener(item, timeLineView);
        }

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position + 1 == getItemCount()) {
            params.setMargins(0, 0, 0, (int) (0.7f * holder.itemView.getContext().getResources().getDimension(R.dimen.bottom_bar_height)));
        } else {
            params.setMargins(0, 0, 0, 0);
        }
        holder.itemView.setLayoutParams(params);
    }

    private void initShowLogClickListener(LogItem item, Button showLogs) {
        showLogs.setOnClickListener(v -> {
            try {
                Navigation
                        .findNavController((Activity) v.getContext(), R.id.main_nav_host_fragment)
                        .navigate(LogsFragmentDirections.actionLogsFragmentToAddLogFragment(item.getDate()));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });
    }

    private void initTimeLineClickListener(LogItem item, TimeLineView timeLineView) {
        timeLineView.setOnClickListener(v -> {
            persianCalendar = CalendarTool
                    .GregorianToPersian(Objects.requireNonNull(Objects.requireNonNull(item.getDate().getCalendar())));
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {

                        max.setTimeInMillis(now.getTimeInMillis());
                        tempPersianDate.setPersianDate(year, monthOfYear + 1, dayOfMonth);
                        temp = CalendarTool.PersianToGregorian(tempPersianDate);

                        boolean invalid = temp.getTimeInMillis() < min.getTimeInMillis()
                                || temp.getTimeInMillis() > max.getTimeInMillis();
                        if (invalid) {
                            Toast.makeText(v.getContext(), v.getContext().getString(R.string.wrong_selected_date), Toast.LENGTH_SHORT).show();

                        } else {
                            try {
                                Date date = new Date(CalendarTool.PersianToGregorian(tempPersianDate), true);
                                Navigation
                                        .findNavController((Activity) v.getContext(), R.id.main_nav_host_fragment)
                                        .navigate(LogsFragmentDirections
                                                .actionLogsFragmentToAddLogFragment(date));
                            } catch (Exception e) {
                                Logger.printStackTrace(e);
                            }
                        }

                    },
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "Datepickerdialog");
        });
    }


    public void setMinDate(Calendar minDate) {
        this.min.setTimeInMillis(minDate.getTimeInMillis());
    }


    public class LogItemViewHolder extends RecyclerView.ViewHolder {
        LogItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<LogItem> {

        @Override
        public boolean areItemsTheSame(@NonNull LogItem oldItem, @NonNull LogItem newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull LogItem oldItem, @NonNull LogItem newItem) {
            return false;
        }
    }
}
