package com.simorgh.pregnancyapp.adapter;

import android.annotation.SuppressLint;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.database.Date;

import androidx.annotation.NonNull;

public class LogItem {
    private Date date;
    private int daysFromStart;

    public LogItem(Date date, Date daysFromStart) {
        this.date = date;
        this.daysFromStart = (int) CalendarTool.getDaysFromDiff(date.getCalendar(), daysFromStart.getCalendar()) + 1;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDaysFromStart() {
        return daysFromStart;
    }

    public void setDaysFromStart(int daysFromStart) {
        this.daysFromStart = daysFromStart;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("{ %s },{ daysFromStart= %d }", date.toString(), daysFromStart);
    }
}
