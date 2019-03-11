package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.ArticleDetailViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.adapter.ArticleDetailAdapter;
import com.simorgh.pregnancyapp.ui.BaseFragment;

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
    private UserViewModel mUserViewModel;

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
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ArticleDetailViewModel.class);
        mUserViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
        if (getArguments() != null) {
            articleType = ArticleDetailFragmentArgs.fromBundle(getArguments()).getArticleType();
            weekNumber = ArticleDetailFragmentArgs.fromBundle(getArguments()).getWeekNumber();
            mViewModel.setArticleType(articleType);
            mViewModel.setWeekNumber(repository, weekNumber);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (articleType == 0) {
            return inflater.inflate(R.layout.fragment_article_detail_mother, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_article_detail_embryo, container, false);
        }
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

//        repository.getWeekArticleItems(1,true);

        mViewModel.getItems().observe(this, articleViewSubItems -> {
            ((ArticleDetailAdapter) Objects.requireNonNull(rvArticleDetail.getAdapter())).submitList(articleViewSubItems);
        });

        mUserViewModel.getFontSize().observe(this, s -> {
            ((ArticleDetailAdapter) Objects.requireNonNull(rvArticleDetail.getAdapter())).setFontSize(Integer.parseInt(s));
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
