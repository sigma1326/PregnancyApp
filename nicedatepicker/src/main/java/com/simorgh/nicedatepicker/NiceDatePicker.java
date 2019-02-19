package com.simorgh.nicedatepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

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
public class NiceDatePicker extends ConstraintLayout {
    private NumberPicker npDay;
    private NumberPicker npMonth;
    private NumberPicker npYear;

    private TextView tvDate;
    private Date selectedDate = null;

    private Calendar calendar;
    private PersianCalendar persianCalendar;
    private PersianCalendar selectedPersianDate;

    private volatile OnDateSelectedListener onDateSelectedListener;

    private volatile Date date;


    {
        ThreadUtils.execute(() -> {
            calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            persianCalendar = CalendarTool.GregorianToPersian(calendar);
            selectedPersianDate = CalendarTool.GregorianToPersian(calendar);

            date = new Date(calendar.getTimeInMillis());
        });

    }

    private static final String[] monthNames = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"};


    public NiceDatePicker(Context context) {
        super(context);
        initView(context, null);
    }

    public NiceDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public NiceDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public NiceDatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        View v = View.inflate(context, R.layout.nice_date_picker_layout, this);

        npDay = v.findViewById(R.id.np_day);
        npMonth = v.findViewById(R.id.np_month);
        npYear = v.findViewById(R.id.np_year);

        tvDate = v.findViewById(R.id.tv_date);


        npMonth.setMinValue(1);
        npMonth.setMaxValue(monthNames.length);
        npMonth.setDisplayedValues(monthNames);
        npMonth.setValue(9);


        updateView();

        npDay.setOnValueChangedListener((picker, oldVal, newVal) -> {
            selectedPersianDate.setPersianDate(selectedPersianDate.getPersianYear(), selectedPersianDate.getPersianMonth(), newVal);
            updateBubbleText();
        });

        npMonth.setOnValueChangedListener((picker, oldVal, newVal) -> {
            selectedPersianDate.setPersianDate(selectedPersianDate.getPersianYear(), newVal - 1, selectedPersianDate.getPersianDay());

            if (newVal <= 6) {
                npDay.setMaxValue(31);
            } else {
                if (newVal == 12) {
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
            updateBubbleText();
        });

        npYear.setOnValueChangedListener((picker, oldVal, newVal) -> {
            selectedPersianDate.setPersianDate(newVal, selectedPersianDate.getPersianMonth(), selectedPersianDate.getPersianDay());
            if (selectedPersianDate.isPersianLeapYear()) {
                if (npMonth.getValue() == 12) {
                    if (npDay.getValue() > 30) {
                        npDay.setValue(30);
                    }
                    npDay.setMaxValue(30);
                }
            } else {
                if (npMonth.getValue() == 12) {
                    if (npDay.getValue() > 29) {
                        npDay.setValue(29);
                    }
                    npDay.setMaxValue(29);
                }
            }
            updateBubbleText();
        });

        updateBubbleText();
    }

    private void updateView() {
        if (selectedPersianDate != null && persianCalendar != null && persianCalendar.getPersianYear() >= 1397) {
            npYear.setMinValue(selectedPersianDate.getPersianYear() - 50);
            npYear.setMaxValue(selectedPersianDate.getPersianYear());
            npYear.setValue(selectedPersianDate.getPersianYear());

            npMonth.setValue(selectedPersianDate.getPersianMonth());

            npDay.setValue(selectedPersianDate.getPersianDay());

            selectedDate = new Date(calendar);
        }
    }

    private void updateBubbleText() {
        if (npYear != null && npMonth != null && npDay != null && tvDate != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(npDay.getValue());
            sb.append("\n");
            if (npMonth.getMaxValue() == monthNames.length) {
                sb.append(monthNames[npMonth.getValue() - 1]);
                sb.append("\n");
            }
            sb.append(npYear.getValue());
            tvDate.setText(sb);
            if (selectedPersianDate != null) {
                selectedPersianDate.setPersianDate(npYear.getValue(), npMonth.getValue(), npDay.getValue());
            }
            ThreadUtils.execute(() -> {
                if (onDateSelectedListener != null) {
                    date.setMillis(CalendarTool.PersianToGregorian(selectedPersianDate).getTimeInMillis());
                    onDateSelectedListener.OnDateSelected(date);
                }
            });
        }
    }

    public Date getSelectedDate() {
        Calendar calendar = CalendarTool.PersianToGregorian(selectedPersianDate);
        selectedDate = new Date(calendar.getTimeInMillis());
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
