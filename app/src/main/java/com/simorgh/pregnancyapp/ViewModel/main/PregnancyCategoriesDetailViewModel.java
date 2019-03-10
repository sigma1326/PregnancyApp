package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Repository;
import com.simorgh.database.callback.ArticlesForTypeCallBack;
import com.simorgh.database.model.ArticleWithParagraph;
import com.simorgh.threadutils.ThreadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

public class PregnancyCategoriesDetailViewModel extends ViewModel {
    private MediatorLiveData<List<ArticleWithParagraph>> articles = new MediatorLiveData<>();
    private String titleText = "";
    private int type = 0;

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void getData(@NonNull Repository repository) {
        repository.getArticlesForType(type, new ArticlesForTypeCallBack() {
            @Override
            public void onSuccess(List<ArticleWithParagraph> articles1) {
                ThreadUtils.onUI(() -> articles.setValue(articles1));
            }

            @Override
            public void onFailed(String error) {

            }
        });
    }

    public MediatorLiveData<List<ArticleWithParagraph>> getArticles() {
        return articles;
    }
}
