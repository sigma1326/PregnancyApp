package com.simorgh.database.model;

import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "articles")
public class Article {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "image_name")
    private String imageName;

    /**
     * 0 for weekly
     * 1 to 4 : categorized articles
     */
    @ColumnInfo(name = "type")
    private int type;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Article other = (Article) obj;
        return getId() == Objects.requireNonNull(other).id
                && getTitle().equals(other.getTitle())
                && imageName.equals(other.getImageName())
                && getType() == other.getType();
    }
}
