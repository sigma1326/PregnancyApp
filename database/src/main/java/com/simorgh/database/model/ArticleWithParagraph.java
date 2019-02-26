package com.simorgh.database.model;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;
import androidx.room.Embedded;
import androidx.room.Relation;

@Keep
public class ArticleWithParagraph {

    @Embedded
    private Article article;

    @Relation(parentColumn = "id", entityColumn = "article_id", entity = Paragraph.class)
    private List<Paragraph> paragraphs;


    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public List<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<Paragraph> paragraphs) {
        if (this.paragraphs == null) {
            this.paragraphs = new ArrayList<>();
        }
        this.paragraphs.addAll(paragraphs);
    }
}
