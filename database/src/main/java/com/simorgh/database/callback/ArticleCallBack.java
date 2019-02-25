package com.simorgh.database.callback;

import com.simorgh.database.model.Article;

public interface ArticleCallBack {
    void onSuccess(Article article);

    void onFailed(String error);
}
