package com.simorgh.nicedatepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.numberpicker.NumberPicker;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

@Keep
public class NiceDatePickerSmall extends ConstraintLayout {
    private NumberPicker npDay;

    private TextView tvDate;

    private final PersianCalendar persianCalendar;
    private PersianCalendar selectedPersianDate;
    private final PersianCalendar tempPersianDate;

    private Date date;

    private int maxYear;
    private int minYear;

    private final Calendar minDate;
    private final Calendar maxDate;

    {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        selectedPersianDate = CalendarTool.GregorianToPersian(calendar);
        tempPersianDate = CalendarTool.GregorianToPersian(calendar);
        persianCalendar = CalendarTool.GregorianToPersian(calendar);


        date = new Date(calendar, true);

        minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -1);

        maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 1);
    }


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
        LayoutInflater.from(context).inflate(R.layout.nice_date_picker_small_layout, this);
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setLayerType(LAYER_TYPE_HARDWARE, null);


        tvDate = findViewById(R.id.tv_date);
        npDay = findViewById(R.id.np_day);


        updateView();
        updateBubbleText();


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
            tempPersianDate.setPersianDate(year, month, newVal);

            if (CalendarTool.getDaysFromDiff(tempPersianDate, maxDate) <= 0 && CalendarTool.getDaysFromDiff(tempPersianDate, minDate) >= 0) {
                selectedPersianDate.setPersianDate(year, month, newVal);
                if (onDateSelectedListener != null) {
                    int y = tempPersianDate.get(Calendar.YEAR);
                    int m = tempPersianDate.get(Calendar.MONTH);
                    int d = tempPersianDate.get(Calendar.DAY_OF_MONTH);
                    date.setYearMonthDay(y, m, d);
                    date.clearHourMinuteSeconds();
                    onDateSelectedListener.OnDateSelected(date);
                }
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

        }
    }

    public void setDateRange(final Calendar min, final Calendar max) {
        if (min != null && maxDate != null) {
            minDate.setTimeInMillis(min.getTimeInMillis());
            maxDate.setTimeInMillis(max.getTimeInMillis());
            PersianCalendar p = CalendarTool.GregorianToPersian(min);
            minYear = p.getPersianYear();
            p = CalendarTool.GregorianToPersian(max);
            maxYear = p.getPersianYear();
            updateView();
            updateBubbleText();
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
        date.setCalendar(selectedPersianDate);
        date.clearHourMinuteSeconds();
        return date;
    }

    public void setSelectedDate(@NonNull final Date selectedDate) {
        this.date = selectedDate;
        selectedPersianDate = CalendarTool.GregorianToPersian(selectedDate.getCalendar());
        updateView();
        updateBubbleText();
    }

    public OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }
}
