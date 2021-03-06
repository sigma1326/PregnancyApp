package com.simorgh.pregnancyapp.View.register;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.bloodtypepicker.BloodTypePicker;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.View.TitleChangeListener;
import com.simorgh.pregnancyapp.ViewModel.register.RegisterViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import java.util.Objects;

public class BloodTypeFragment extends BaseFragment {

    private RegisterViewModel mViewModel;
    private TitleChangeListener titleChangeListener;

    @BindView(R.id.blood_type_picker)
    BloodTypePicker bloodTypePicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blood_type, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(titleChangeListener).onTitleChanged(getString(R.string.select_blood_type));

        if (mViewModel.getBloodType().getValue() == null) {
            mViewModel.setBloodType(bloodTypePicker.getBloodType());
        } else {
            bloodTypePicker.setBloodType(mViewModel.getBloodType().getValue());
        }
        bloodTypePicker.setBloodTypePickedListener(bloodType -> mViewModel.setBloodType(bloodType));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            titleChangeListener = (TitleChangeListener) context;
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    @Override
    public void onDetach() {
        titleChangeListener = null;
        super.onDetach();
    }

}
