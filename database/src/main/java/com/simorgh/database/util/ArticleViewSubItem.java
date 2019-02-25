package com.simorgh.database.util;

public class ArticleViewSubItem {
    private int articleID;
    private ArticleSubItemType type;
    private String content;

    public ArticleViewSubItem(int articleID, ArticleSubItemType type, String content) {
        this.articleID = articleID;
        this.type = type;
        this.content = content;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public ArticleSubItemType getType() {
        return type;
    }

    public void setType(ArticleSubItemType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
