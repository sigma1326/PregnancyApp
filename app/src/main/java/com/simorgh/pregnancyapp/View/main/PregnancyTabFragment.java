package com.simorgh.pregnancyapp.View.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.PregnancyTabViewModel;
import com.squareup.haha.perflib.Main;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

public class PregnancyTabFragment extends Fragment {

    private PregnancyTabViewModel mViewModel;
    private Button mFitnessBtn;
    private Button mGeneralBtn;
    private Button mFaqBtn;
    private Button mGiveBirthBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pregnancy_tab_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PregnancyTabViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);

        mFitnessBtn = view.findViewById(R.id.btn_fitness);
        mGeneralBtn = view.findViewById(R.id.btn_general);
        mFaqBtn = view.findViewById(R.id.btn_faq);
        mGiveBirthBtn = view.findViewById(R.id.btn_give_birth);

        mFitnessBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(R.id.action_articlesFragment_to_pregnancyCategoriesDetailFragment);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }

        });
        mGeneralBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(R.id.action_articlesFragment_to_pregnancyCategoriesDetailFragment);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });
        mFaqBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(R.id.action_articlesFragment_to_pregnancyCategoriesDetailFragment);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });
        mGiveBirthBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(R.id.action_articlesFragment_to_pregnancyCategoriesDetailFragment);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });


    }
}
