package com.simorgh.nicedatepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.numberpicker.NumberPicker;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

@Keep
public class NiceDatePickerSmall extends ConstraintLayout {
    private NumberPicker npDay;

    private TextView tvDate;
    private Date selectedDate = null;

    private volatile Calendar calendar;
    private volatile Calendar calendarMin;
    private volatile PersianCalendar persianCalendar;
    private volatile PersianCalendar selectedPersianDate;
    private volatile PersianCalendar tempPersianDate;

    private volatile Date date;

    private volatile int maxYear;
    private volatile int minYear;


    private volatile OnDateSelectedListener onDateSelectedListener;


    private static final String[] monthNames = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};


    public NiceDatePickerSmall(Context context) {
        super(context);
        initView(context, null);
    }

    public NiceDatePickerSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public NiceDatePickerSmall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public NiceDatePickerSmall(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        View v = View.inflate(context, R.layout.nice_date_picker_small_layout, this);


        tvDate = v.findViewById(R.id.tv_date);
        npDay = v.findViewById(R.id.np_day);


        ThreadUtils.execute(() -> {
            calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            calendarMin = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            persianCalendar = CalendarTool.GregorianToPersian(calendar);
            tempPersianDate = CalendarTool.GregorianToPersian(calendar);
            selectedPersianDate = CalendarTool.GregorianToPersian(calendar);
            minYear = selectedPersianDate.getPersianYear() - 50;
            maxYear = selectedPersianDate.getPersianYear();

            calendarMin.add(Calendar.YEAR, -50);

            try {
                date = new Date(calendar.getTimeInMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ThreadUtils.runOnUIThread(() -> {
                updateView();
                updateBubbleText();
            }, 0);
        });


        npDay.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int month = selectedPersianDate.getPersianMonth() + 1;
            int year = selectedPersianDate.getPersianYear();


            if (newVal == npDay.getMinValue()) {
                if (oldVal == npDay.getMaxValue()) {
                    month++;
                    if (month == 13) {
                        month = 1;
                        year++;
                        if (year > maxYear) {
                            year--;
                            month = 12;
                            npDay.setValue(oldVal);
                        }
                    }
                }
            } else if (newVal == npDay.getMaxValue()) {
                if (oldVal == npDay.getMinValue()) {
                    month--;
                    if (month == 0) {
                        year--;
                        month = 12;
                        if (year < minYear) {
                            year++;
                            month = 1;
                            npDay.setValue(oldVal);
                        }
                    }
                }
            }

            updateDayPickerRange(month);
            tempPersianDate.setPersianDate(year, month, npDay.getValue());
            if (CalendarTool.getDaysFromDiff(tempPersianDate, calendar) <= 0 && CalendarTool.getDaysFromDiff(tempPersianDate, calendarMin) >= 0) {
                selectedPersianDate.setPersianDate(year, month, npDay.getValue());
                ThreadUtils.execute(() -> {
                    if (onDateSelectedListener != null) {
                        date.setCalendar(CalendarTool.PersianToGregorian(selectedPersianDate));
                        date.clearHourMinuteSeconds();
                        onDateSelectedListener.OnDateSelected(date);
                    }
                });
                updateBubbleText();
            } else {
                npDay.setValue(oldVal);
                Toast.makeText(getContext(), getContext().getString(R.string.can_not_be_selected), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateView() {
        if (selectedPersianDate != null && persianCalendar != null && persianCalendar.getPersianYear() >= 1397) {


            npDay.setValue(selectedPersianDate.getPersianDay());
            int month = selectedPersianDate.getPersianMonth() + 1;

            updateDayPickerRange(month);

            selectedDate = new Date(calendar);
        }
    }

    private void updateDayPickerRange(final int month) {
        if (month <= 6) {
            npDay.setMaxValue(31);
        } else {
            if (month == 12) {
                if (selectedPersianDate.isPersianLeapYear()) {
                    if (npDay.getValue() > 30) {
                        npDay.setValue(30);
                    }
                    npDay.setMaxValue(30);
                } else {
                    if (npDay.getValue() > 29) {
                        npDay.setValue(29);
                    }
                    npDay.setMaxValue(29);
                }
            } else {
                npDay.setMaxValue(30);
            }
        }
    }

    private void updateBubbleText() {
        if (npDay != null && tvDate != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(npDay.getValue());
            sb.append("\n");
            if (selectedPersianDate != null) {
                sb.append(monthNames[selectedPersianDate.getPersianMonth()]);
                sb.append("\n");
                sb.append(selectedPersianDate.getPersianYear());
            }
            tvDate.setText(sb);
        }
    }

    public Date getSelectedDate() {
        Calendar calendar = CalendarTool.PersianToGregorian(selectedPersianDate);
        selectedDate = new Date(calendar);
        selectedDate.clearHourMinuteSeconds();
        return selectedDate;
    }

    public void setSelectedDate(@NonNull final Date selectedDate) {
        this.selectedDate = selectedDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedDate.getMilli());
        selectedPersianDate = CalendarTool.GregorianToPersian(calendar);
        updateView();
    }

    public OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }
}
