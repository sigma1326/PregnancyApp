package com.simorgh.database.model;

import com.simorgh.database.Date;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "exercise_time")
public class ExerciseTime {
    @ColumnInfo(name = "minute")
    private float minute;

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

    public float getMinute() {
        return minute;
    }

    public void setMinute(float minute) {
        this.minute = minute;
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
}
