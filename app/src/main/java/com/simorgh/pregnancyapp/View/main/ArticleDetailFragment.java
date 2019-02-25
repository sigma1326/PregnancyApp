package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simorgh.database.callback.ArticleCallBack;
import com.simorgh.database.callback.ParagraphsCallBack;
import com.simorgh.database.model.Article;
import com.simorgh.database.model.Paragraph;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.Model.AppManager;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.ArticleDetailViewModel;
import com.simorgh.pregnancyapp.adapter.ArticleDetailAdapter;
import com.simorgh.database.util.ArticleSubItemType;
import com.simorgh.database.util.ArticleViewSubItem;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class ArticleDetailFragment extends BaseFragment {

    private ArticleDetailViewModel mViewModel;

    @BindView(R.id.tv_app_title)
    TextView title;

    @BindView(R.id.img_back)
    ImageButton backButton;

    private int articleType;
    private int weekNumber;

    @BindView(R.id.rv_article_detail)
    RecyclerView rvArticleDetail;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getDaggerApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleType = ArticleDetailFragmentArgs.fromBundle(getArguments()).getArticleType();
            weekNumber = ArticleDetailFragmentArgs.fromBundle(getArguments()).getWeekNumber();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (articleType == 0) {
            return inflater.inflate(R.layout.article_detail_fragment_mother, container, false);
        } else {
            return inflater.inflate(R.layout.article_detail_fragment_embryo, container, false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ArticleDetailViewModel.class);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment).navigateUp();
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        String temp = articleType == 0 ? "مادر" : "جنین";
        title.setText(String.format("اطلاعات مربوط به %s در هفته %s ام", temp, weekNumber));

        initRecyclerView();

    }

    private void initRecyclerView() {
        rvArticleDetail.setAdapter(new ArticleDetailAdapter(new ArticleDetailAdapter.ItemDiffCallBack()));
        rvArticleDetail.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvArticleDetail.setHasFixedSize(true);
        rvArticleDetail.setNestedScrollingEnabled(false);

        if (repository != null) {

//            repository.getArticleDetailItems(1);

            repository.getWeekArticle(weekNumber, articleType == 0,new ArticleCallBack() {
                @Override
                public void onSuccess(Article article) {
                    ArrayList<ArticleViewSubItem> arrayList = new ArrayList<>();
                    ArticleViewSubItem title = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.title, article.getTitle());
                    ArticleViewSubItem image = new ArticleViewSubItem((int) article.getId(), ArticleSubItemType.image, article.getImageName());
                    arrayList.add(title);


                    repository.getParagraphs(article.getId(), new ParagraphsCallBack() {
                        @Override
                        public void onSuccess(List<Paragraph> paragraphs) {
                            for (int i = 0; i < paragraphs.size(); i++) {
                                ArticleViewSubItem p = new ArticleViewSubItem((int) article.getId()
                                        , ArticleSubItemType.paragraph, paragraphs.get(i).getContent());
                                arrayList.add(p);
                                if (i == 0) {
                                    arrayList.add(image);
                                }
                            }
                            ((ArticleDetailAdapter) Objects.requireNonNull(rvArticleDetail.getAdapter())).submitList(arrayList);
                        }

                        @Override
                        public void onFailed(String error) {
                            ((ArticleDetailAdapter) Objects.requireNonNull(rvArticleDetail.getAdapter())).submitList(arrayList);
                        }
                    });
                }

                @Override
                public void onFailed(String error) {

                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
