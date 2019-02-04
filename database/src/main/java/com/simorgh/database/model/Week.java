package com.simorgh.database.model;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Keep
@Entity(tableName = "weeks", foreignKeys = {
        @ForeignKey(entity = Article.class,
                parentColumns = "id",
                childColumns = "mother_article_id",
                onDelete = CASCADE,
                onUpdate = CASCADE)
        , @ForeignKey(entity = Article.class,
        parentColumns = "id",
        childColumns = "embryo_article_id",
        onDelete = CASCADE,
        onUpdate = CASCADE)
}, indices = {@Index(value = "mother_article_id"), @Index(value = "embryo_article_id")})
public class Week {
    @ColumnInfo(name = "week_number")
    @PrimaryKey()
    private int weekNumber;

    @ColumnInfo(name = "embryo_summary")
    private String embryoSummary;


    @ColumnInfo(name = "embryo_info")
    private String embryoInfo;

    @ColumnInfo(name = "mother_info")
    private String motherInfo;


    @ColumnInfo(name = "embryo_article_id")
    private int embryoArticleID;

    @ColumnInfo(name = "mother_article_id")
    private int motherArticleID;


    @ColumnInfo(name = "embryo_image_name")
    private String embryoImageName;

    @ColumnInfo(name = "mother_image_name")
    private String motherImageName;

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getEmbryoSummary() {
        return embryoSummary;
    }

    public void setEmbryoSummary(String embryoSummary) {
        this.embryoSummary = embryoSummary;
    }

    public String getEmbryoInfo() {
        return embryoInfo;
    }

    public void setEmbryoInfo(String embryoInfo) {
        this.embryoInfo = embryoInfo;
    }

    public String getMotherInfo() {
        return motherInfo;
    }

    public void setMotherInfo(String motherInfo) {
        this.motherInfo = motherInfo;
    }

    public int getEmbryoArticleID() {
        return embryoArticleID;
    }

    public void setEmbryoArticleID(int embryoArticleID) {
        this.embryoArticleID = embryoArticleID;
    }

    public int getMotherArticleID() {
        return motherArticleID;
    }

    public void setMotherArticleID(int motherArticleID) {
        this.motherArticleID = motherArticleID;
    }

    public String getEmbryoImageName() {
        return embryoImageName;
    }

    public void setEmbryoImageName(String embryoImageName) {
        this.embryoImageName = embryoImageName;
    }

    public String getMotherImageName() {
        return motherImageName;
    }

    public void setMotherImageName(String motherImageName) {
        this.motherImageName = motherImageName;
    }
}
