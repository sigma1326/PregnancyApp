package com.simorgh.pregnancyapp.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.MakeReportViewModel;

public class MakeReportFragment extends Fragment {

    private MakeReportViewModel mViewModel;

    public static MakeReportFragment newInstance() {
        return new MakeReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.make_report_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MakeReportViewModel.class);
    }

}
