package com.simorgh.pregnancyapp.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.LogsViewModel;

public class LogsFragment extends Fragment {

    private LogsViewModel mViewModel;
    private ViewStub viewStub;
    private TextView emptyLogs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logs_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LogsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewStub = view.findViewById(R.id.viewStub);
        viewStub.setLayoutResource(R.layout.logs_empty_layout);
        viewStub.inflate();
        emptyLogs = view.findViewById(R.id.tv_no_logged_data);
        emptyLogs.animate()
                .scaleXBy(0.9f)
                .scaleYBy(0.9f)
                .setStartDelay(300)
                .setInterpolator(new BounceInterpolator())
                .setDuration(400)
                .start();
    }


    @Override
    public void onDestroyView() {
        viewStub = null;
        super.onDestroyView();
    }
}
