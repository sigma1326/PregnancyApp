package com.simorgh.database.model;

import com.simorgh.database.Date;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "sleep_time")
public class SleepTime {
    @ColumnInfo(name = "hour")
    private float hour;

    @ColumnInfo(name = "info")
    private String info;

    @PrimaryKey
    @ColumnInfo(name = "date")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setEvaluate(boolean b) {
        evaluate = b;
    }

    public boolean evaluate() {
        return evaluate;
    }

    @Ignore
    private boolean evaluate = false;

    @Ignore
    public void clear() {
        hour = 0;
        info = null;
        date = null;
        evaluate = false;
    }

    @Ignore
    public void set(SleepTime value) {
        date = value.getDate();
        info = value.getInfo();
        hour = value.getHour();
        evaluate = value.evaluate;
    }
}
