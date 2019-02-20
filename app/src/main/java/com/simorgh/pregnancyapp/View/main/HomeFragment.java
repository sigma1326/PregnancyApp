package com.simorgh.pregnancyapp.View.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.HomeViewModel;
import com.simorgh.threadutils.ThreadUtils;
import com.simorgh.weekslider.WeekSlider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private MotionLayout motionLayout;
    private WeekSlider weekSlider;
    private Guideline guideline;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        motionLayout = (MotionLayout) view;
        ThreadUtils.runOnUIThread(() -> motionLayout.transitionToEnd(), 100);
        weekSlider = view.findViewById(R.id.week_slider);
        guideline = view.findViewById(R.id.guildline);

        weekSlider.setStartTextDay("1");
        weekSlider.setStartTextMonth("شهریور");

        weekSlider.setEndTextDay("26");
        weekSlider.setEndTextMonth("اردیبهشت");


        weekSlider.setStateUpdateListener((percent, weekNumber) -> {
            guideline.setGuidelinePercent(1 - 0.45f * percent);
        });
    }

    @Override
    public void onDestroyView() {
        if (motionLayout != null) {
            motionLayout.transitionToStart();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        motionLayout = null;
        weekSlider.setStateUpdateListener(null);
        weekSlider = null;
        guideline = null;
    }
}
