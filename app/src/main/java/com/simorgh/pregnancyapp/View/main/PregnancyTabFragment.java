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
import com.simorgh.pregnancyapp.ViewModel.PregnancyTabViewModel;

public class PregnancyTabFragment extends Fragment {

    private PregnancyTabViewModel mViewModel;

    public static PregnancyTabFragment newInstance() {
        return new PregnancyTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pregnancy_tab_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PregnancyTabViewModel.class);
    }

}
