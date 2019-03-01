package com.simorgh.database.model;

import com.simorgh.database.Date;

import java.util.Objects;
import java.util.Random;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "drugs")
public class Drug {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "drug_name")
    private String drugName;

    @ColumnInfo(name = "info")
    private String info;

    @Ignore
    public Drug(String drugName, String info) {
        this.drugName = drugName;
        this.info = info;
    }

    public Drug() {
        date = new Date(1970, 0, 1, 0, 0, 0);
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

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;//todo fix this
        }
        return ((Drug) obj).getId() == getId();
//        return ((Drug) obj).getDrugName().equals(getDrugName()) && ((Drug) obj).getInfo().equals(getInfo()) && ((Drug) obj).getDate().equals(getDate());
    }

    public boolean isSameContent(@Nullable Drug drug) {
        if (getDate() != null && Objects.requireNonNull(drug).getDate() != null) {
            return getId() == Objects.requireNonNull(drug).getId() &&
                    getDrugName().equals(drug.getDrugName()) &&
                    getDate().equals(drug.getDate()) &&
                    getInfo().equals(drug.getInfo());
        } else {
            return getId() == Objects.requireNonNull(drug).getId() &&
                    getDrugName().equals(drug.getDrugName()) &&
                    getInfo().equals(drug.getInfo());
        }

    }
}
