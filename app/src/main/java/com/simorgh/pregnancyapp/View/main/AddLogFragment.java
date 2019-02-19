package com.simorgh.pregnancyapp.View.main;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;

import com.simorgh.logger.Logger;
import com.simorgh.nicedatepicker.NiceDatePickerSmall;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.View.TitleChangeListener;
import com.simorgh.pregnancyapp.View.register.RegisterActivity;
import com.simorgh.pregnancyapp.ViewModel.main.AddLogViewModel;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Objects;

public class AddLogFragment extends Fragment {

    private AddLogViewModel mViewModel;
    private ImageButton backButton;
    private NiceDatePickerSmall datePicker;
    private ViewStub viewStub;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_log_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddLogViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewStub = view.findViewById(R.id.view_stub_add_log);
        viewStub.inflate();


        backButton = view.findViewById(R.id.img_back);
        datePicker = view.findViewById(R.id.date_picker);


        backButton.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigateUp();
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }

        });


    }

    @Override
    public void onDestroy() {
        backButton = null;
        datePicker = null;
        viewStub = null;
        super.onDestroy();
    }
}
