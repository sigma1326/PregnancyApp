package com.simorgh.database.model;

import com.simorgh.database.Date;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "users")
public class User {
    @ColumnInfo(name = "id")
    @PrimaryKey()
    private int id;

    @ColumnInfo(name = "font_size")
    private int fontSize;

    @ColumnInfo(name = "blood_type")
    private String bloodType;

    @ColumnInfo(name = "is_negative")
    private boolean isNegative;

    @ColumnInfo(name = "pregnancy_start_date")
    private Date pregnancyStartDate;

    @ColumnInfo(name = "birth_date")
    private Date birthDate;


    public User() {
        id = 1;
        fontSize = 14;
        isNegative = false;
        bloodType = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean negative) {
        isNegative = negative;
    }

    public Date getPregnancyStartDate() {
        return pregnancyStartDate;
    }

    public void setPregnancyStartDate(Date pregnancyStartDate) {
        this.pregnancyStartDate = pregnancyStartDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
