package com.simorgh.pregnancyapp.View.main;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.HomeViewModel;
import com.simorgh.threadutils.ThreadUtils;


public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private MotionLayout motionLayout;


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
        ThreadUtils.runOnUIThread(()-> motionLayout.transitionToEnd(),100);
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
    }
}
