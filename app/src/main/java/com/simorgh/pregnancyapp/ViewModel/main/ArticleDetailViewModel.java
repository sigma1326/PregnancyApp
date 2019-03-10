package com.simorgh.pregnancyapp.ViewModel.main;

import com.simorgh.database.Repository;
import com.simorgh.database.model.Article;
import com.simorgh.database.model.Paragraph;
import com.simorgh.database.util.ArticleSubItemType;
import com.simorgh.database.util.ArticleViewSubItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class ArticleDetailViewModel extends ViewModel {
    private int articleType;
    private MediatorLiveData<Integer> weekNumber = new MediatorLiveData<>();
    private LiveData<List<ArticleViewSubItem>> items = new MutableLiveData<>();
    private LiveData<List<Paragraph>> paragraphs;
    private LiveData<Article> article = new MediatorLiveData<>();

    public LiveData<Integer> getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Repository repository, int value) {
        this.weekNumber.setValue(value);
        if (repository != null) {
            article = repository.getWeekArticle(value, articleType == 0);

            paragraphs = Transformations.switchMap(article, input -> {
                if (input != null) {
                    return repository.getParagraphsLiveData(input.getId());
                } else {
                    return null;
                }
            });

            items = Transformations.map(paragraphs, paragraphs -> {
                if (paragraphs != null) {
                    List<ArticleViewSubItem> list = new ArrayList<>();
                    Article art = article.getValue();
                    ArticleViewSubItem title;
                    ArticleViewSubItem image = null;
                    if (art != null) {
                        title = new ArticleViewSubItem((int) art.getId(), ArticleSubItemType.title, art.getTitle());
                        image = new ArticleViewSubItem((int) art.getId(), ArticleSubItemType.image, art.getImageName());
                        list.add(title);
                    }
                    if (!paragraphs.isEmpty()) {
                        for (int i = 0; i < Objects.requireNonNull(paragraphs).size(); i++) {
                            ArticleViewSubItem p = new ArticleViewSubItem(paragraphs.get(0).getArticleID()
                                    , ArticleSubItemType.paragraph, paragraphs.get(i).getContent());
                            list.add(p);
                            if (i == 0) {
                                if (image != null) {
                                    list.add(image);
                                }
                            }
                        }
                    }
                    return list;
                }
                return null;
            });

        }

    }

    public LiveData<List<ArticleViewSubItem>> getItems() {
        return items;
    }

    public int getArticleType() {
        return articleType;
    }

    public void setArticleType(int articleType) {
        this.articleType = articleType;
    }
}
