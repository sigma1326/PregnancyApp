package com.simorgh.database.model;

import com.simorgh.database.Date;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "alcohol")
public class Alcohol {
    @ColumnInfo(name = "use_alcohol")
    private boolean useAlcohol;

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

    public boolean isUseAlcohol() {
        return useAlcohol;
    }

    public void setUseAlcohol(boolean useAlcohol) {
        this.useAlcohol = useAlcohol;
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
    private boolean hasData = false;

    @Ignore
    public void clear() {
        useAlcohol = false;
        info = null;
        date = null;
        evaluate = false;
        hasData = false;
    }

    public void set(Alcohol value) {
        useAlcohol = value.isUseAlcohol();
        info = value.getInfo();
        date = value.getDate();
        evaluate = value.evaluate;
        hasData = value.hasData;
    }

    public boolean hasData() {
        return hasData;
    }

    public void setHasData(boolean hasData) {
        this.hasData = hasData;
    }
}
