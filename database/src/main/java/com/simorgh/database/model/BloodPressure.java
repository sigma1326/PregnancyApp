package com.simorgh.database.model;

import com.simorgh.database.Date;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "blood_pressure")
public class BloodPressure {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "min_pressure")
    private float minPressure;

    @ColumnInfo(name = "max_pressure")
    private float maxPressure;

    @ColumnInfo(name = "info")
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getMinPressure() {
        return minPressure;
    }

    public void setMinPressure(float minPressure) {
        this.minPressure = minPressure;
    }

    public float getMaxPressure() {
        return maxPressure;
    }

    public void setMaxPressure(float maxPressure) {
        this.maxPressure = maxPressure;
    }
}
