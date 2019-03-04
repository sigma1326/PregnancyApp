package com.simorgh.database.model;

import com.simorgh.database.Date;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "weights")
public class Weight {
    @ColumnInfo(name = "weight")
    private float weight;

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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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
        weight = 0;
        info = null;
        date = null;
        evaluate = false;
    }

    @Ignore
    public void set(Weight value) {
        date = value.getDate();
        info = value.getInfo();
        weight = value.getWeight();
        evaluate = value.evaluate;
    }
}
