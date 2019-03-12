package com.simorgh.nicedatepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.numberpicker.NumberPicker;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;

@Keep
public class NiceDatePicker extends ConstraintLayout {
    private NumberPicker npDay;
    private NumberPicker npMonth;
    private NumberPicker npYear;

    private TextView tvDate;

    private volatile PersianCalendar selectedPersianDate;
    private volatile PersianCalendar tempPersianDate;


    private volatile OnDateSelectedListener onDateSelectedListener;

    private volatile Date date;

    private volatile Calendar minDate;
    private volatile Calendar maxDate;

    private final int currentPersianYear;


    {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        selectedPersianDate = CalendarTool.GregorianToPersian(calendar);
        currentPersianYear = selectedPersianDate.getPersianYear();
        tempPersianDate = CalendarTool.GregorianToPersian(calendar);

        date = new Date(calendar);

        minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -1);

        maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 1);

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
        LayoutInflater.from(context).inflate(R.layout.nice_date_picker_layout, this);
        ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR);

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setLayerType(LAYER_TYPE_HARDWARE, null);

        npDay = findViewById(R.id.np_day);
        npMonth = findViewById(R.id.np_month);
        npYear = findViewById(R.id.np_year);

        tvDate = findViewById(R.id.tv_date);


        npMonth.setMinValue(1);
        npMonth.setMaxValue(monthNames.length);
        npMonth.setDisplayedValues(monthNames);

        npDay.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int year = selectedPersianDate.getPersianYear();
            int month = selectedPersianDate.getPersianMonth() + 1;
            tempPersianDate.setPersianDate(year, month, newVal);
            boolean invalid = tempPersianDate.before(minDate) || tempPersianDate.after(maxDate);

            if (invalid) {
                npDay.setValue(oldVal);
                Toast.makeText(getContext(), getContext().getString(R.string.can_not_be_selected), Toast.LENGTH_SHORT).show();
            } else {
                selectedPersianDate.setPersianDate(year, month, newVal);
                updateBubbleText(true);
            }
        });

        npMonth.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int year = selectedPersianDate.getPersianYear();
            int month = newVal;
            int day = selectedPersianDate.getPersianDay();
            tempPersianDate.setPersianDate(year, month, day);

            boolean invalid = tempPersianDate.before(minDate) || tempPersianDate.after(maxDate);
            if (invalid) {
                npMonth.setValue(oldVal);
                Toast.makeText(getContext(), getContext().getString(R.string.can_not_be_selected), Toast.LENGTH_SHORT).show();
            } else {
                selectedPersianDate.setPersianDate(year, month, day);
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
                updateBubbleText(true);
            }

        });

        npYear.setOnValueChangedListener((picker, oldVal, newVal) -> {
            int year = newVal;
            int month = selectedPersianDate.getPersianMonth() + 1;
            int day = selectedPersianDate.getPersianDay();
            tempPersianDate.setPersianDate(year, month, day);
            boolean invalid = tempPersianDate.before(minDate) || tempPersianDate.after(maxDate);

            if (invalid) {
                npYear.setValue(oldVal);
                Toast.makeText(getContext(), getContext().getString(R.string.can_not_be_selected), Toast.LENGTH_SHORT).show();
            } else {
                selectedPersianDate.setPersianDate(year, month, day);
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
                updateBubbleText(true);
            }

        });

        updateView();
        updateBubbleText(false);
    }

    private void updateView() {
        if (selectedPersianDate != null && currentPersianYear >= 1397) {
            npYear.setMinValue(CalendarTool.GregorianToPersian(minDate).getPersianYear());
            npYear.setMaxValue(CalendarTool.GregorianToPersian(maxDate).getPersianYear());


            npYear.setValue(selectedPersianDate.getPersianYear());
            npMonth.setValue(selectedPersianDate.getPersianMonth() + 1);
            npDay.setValue(selectedPersianDate.getPersianDay());
        }
    }

    private void updateBubbleText(boolean updateListener) {
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
                Logger.i(updateListener + " : " + selectedPersianDate.getPersianLongDateAndTime());
            }
            if (date != null) {
                date.setCalendar(CalendarTool.PersianToGregorian(selectedPersianDate));
                date.clearHourMinuteSeconds();
                if (updateListener && onDateSelectedListener != null) {
                    onDateSelectedListener.OnDateSelected(date);
                }
            }
        }
    }

    public Date getSelectedDate() {
        return date;
    }

    public void setSelectedDate(@NonNull final Date selectedDate) {
        selectedPersianDate = CalendarTool.GregorianToPersian(selectedDate.getCalendar());
        updateView();
        updateBubbleText(false);
    }

    public void setMinDate(@NonNull Calendar minDate) {
        this.minDate = minDate;
        ThreadUtils.onUI(() -> {
            updateView();
            updateBubbleText(false);
        });
    }

    public void setMaxDate(@NonNull Calendar maxDate) {
        this.maxDate.setTimeInMillis(maxDate.getTimeInMillis());
        ThreadUtils.onUI(() -> {
            updateView();
            updateBubbleText(false);
        });
    }

    public void setDateRange(@NonNull Calendar minDate, @NonNull Calendar maxDate) {
        this.minDate.setTimeInMillis(minDate.getTimeInMillis());
        this.maxDate.setTimeInMillis(maxDate.getTimeInMillis());
        ThreadUtils.onUI(() -> {
            updateView();
            updateBubbleText(false);
        });
    }

    public OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }
}
