package com.simorgh.pregnancyapp.View.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.ArticlesViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.threadutils.ThreadUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class ArticlesFragment extends BaseFragment {

    private ArticlesViewModel mViewModel;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private SectionsPagerAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_articles, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ArticlesViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);


        adapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = view.findViewById(R.id.container);
        tabLayout = view.findViewById(R.id.tabs);


        ThreadUtils.onUI(() -> {
            adapter.clear();

            PregnancyTabFragment pregnancyTabFragment = new PregnancyTabFragment();
            adapter.addFragment(pregnancyTabFragment, "بارداری");


            WeeksInfoTabFragment weeksInfoTabFragment = new WeeksInfoTabFragment();
            adapter.addFragment(weeksInfoTabFragment, "هفتگی");


            mViewPager.setAdapter(adapter);


            tabLayout.setupWithViewPager(mViewPager);
        });
    }


    @Override
    public void onDestroyView() {
        tabLayout = null;
        mViewPager = null;
        adapter = null;
        super.onDestroyView();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void clear() {
            mFragmentList.clear();
            mFragmentTitleList.clear();
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
