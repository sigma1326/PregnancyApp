package com.simorgh.database.callback;

import com.simorgh.database.model.ArticleWithParagraph;
import com.simorgh.database.model.Paragraph;

import java.util.List;

public interface ArticlesForTypeCallBack {
    void onSuccess(List<ArticleWithParagraph> articles);

    void onFailed(String error);
}
