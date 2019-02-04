package com.simorgh.database.model;

import androidx.annotation.Keep;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Keep
@Entity(tableName = "paragraphs", foreignKeys = @ForeignKey(entity = Article.class,
        parentColumns = "id",
        childColumns = "article_id",
        onDelete = CASCADE,
        onUpdate = CASCADE), indices = {@Index(value = "article_id")})
public class Paragraph {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "article_id")
    private int articleID;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "paragraph_number")
    private int paragraphNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public void setParagraphNumber(int paragraphNumber) {
        this.paragraphNumber = paragraphNumber;
    }
}
