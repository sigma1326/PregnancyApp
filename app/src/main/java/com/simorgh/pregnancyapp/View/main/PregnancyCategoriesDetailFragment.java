package com.simorgh.pregnancyapp.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.View.TitleChangeListener;
import com.simorgh.pregnancyapp.ViewModel.main.PregnancyCategoriesDetailViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.adapter.CategoryAdapter;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import java.util.Objects;

public class PregnancyCategoriesDetailFragment extends BaseFragment {

    private PregnancyCategoriesDetailViewModel mViewModel;
    private UserViewModel mUserViewModel;

    @BindView(R.id.tv_app_title)
    TextView title;

    @BindView(R.id.img_back)
    ImageButton back;

    @BindView(R.id.rv_categories)
    RecyclerView categories;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pregnancy_categories_detail_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PregnancyCategoriesDetailViewModel.class);
        mUserViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
        if (getArguments() != null) {
            mViewModel.setTitleText(PregnancyCategoriesDetailFragmentArgs.fromBundle(getArguments()).getTitle());
            mViewModel.setType(PregnancyCategoriesDetailFragmentArgs.fromBundle(getArguments()).getCategoryType());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        back.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment).navigateUp();
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });


        title.setText(mViewModel.getTitleText());


        categories.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        categories.setHasFixedSize(true);
        categories.setNestedScrollingEnabled(false);
        categories.setAdapter(new CategoryAdapter(new CategoryAdapter.ItemDiffCallBack(),
                new CategoryAdapter.ItemClickListener() {
                    @NonNull
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                }));


        mViewModel.getData(repository);


        mViewModel.getArticles().observe(this, articles -> {
            ((CategoryAdapter) Objects.requireNonNull(categories.getAdapter())).submitList(articles);
        });

        mUserViewModel.getFontSize().observe(this,s -> {
            ((CategoryAdapter) Objects.requireNonNull(categories.getAdapter())).setFontSize(Integer.parseInt(s));
        });
    }

}
