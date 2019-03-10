package com.simorgh.pregnancyapp.View.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.simorgh.database.Date;
import com.simorgh.database.model.Drug;
import com.simorgh.logger.Logger;
import com.simorgh.nicedatepicker.NiceDatePickerSmall;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.AddLogViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.adapter.DrugAdapter;
import com.simorgh.pregnancyapp.ui.AlcoholView;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.pregnancyapp.ui.BloodPressureView;
import com.simorgh.pregnancyapp.ui.CigaretteView;
import com.simorgh.pregnancyapp.ui.DrugInsertView;
import com.simorgh.pregnancyapp.ui.ExerciseView;
import com.simorgh.pregnancyapp.ui.FeverView;
import com.simorgh.pregnancyapp.ui.MotherWeightView;
import com.simorgh.pregnancyapp.ui.SleepView;
import com.simorgh.pregnancyapp.utils.Utils;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class AddLogFragment extends BaseFragment {

    private AddLogViewModel mViewModel;
    private UserViewModel mUserViewModel;

    @BindView(R.id.img_back)
    ImageButton backButton;


    @BindView(R.id.date_picker)
    NiceDatePickerSmall datePicker;

    @BindView(R.id.drug_insert_view)
    DrugInsertView drugInsertView;

    @BindView(R.id.rv_drugs)
    RecyclerView rvDrugs;

    @BindView(R.id.blood_pressure_view)
    BloodPressureView bloodPressureView;

    @BindView(R.id.mother_weight_view)
    MotherWeightView motherWeightView;

    @BindView(R.id.fever_view)
    FeverView feverView;

    @BindView(R.id.cigarette_view)
    CigaretteView cigaretteView;

    @BindView(R.id.alcohol_view)
    AlcoholView alcoholView;

    @BindView(R.id.sleep_view)
    SleepView sleepView;

    @BindView(R.id.exercise_view)
    ExerciseView exerciseView;

    @BindView(R.id.btn_apply_changes)
    Button saveLog;

    @BindView(R.id.clear_fields)
    ImageButton clearFields;


    private final Date now = new Date(Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()));

    {
        now.clearHourMinuteSeconds();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddLogViewModel.class);
        mUserViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
        mViewModel.setRepository(repository);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_log_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        backButton.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigateUp();
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }

        });

        clearFields.setOnClickListener(v -> {
            mViewModel.clearFields();
        });


        ThreadUtils.onUI(() -> {
            datePicker.setDateRange(Objects.requireNonNull(mUserViewModel.getUser().getValue()).getPregnancyStartDate().getCalendar(), now.getCalendar());
            if (getArguments() != null) {
                datePicker.setSelectedDate(AddLogFragmentArgs.fromBundle(getArguments()).getSelectedDate());
                mViewModel.setDate(AddLogFragmentArgs.fromBundle(getArguments()).getSelectedDate());
                mViewModel.setInputDate(AddLogFragmentArgs.fromBundle(getArguments()).getSelectedDate());
            } else {
                datePicker.setSelectedDate(now);
                mViewModel.setDate(now);
            }
        }, 200);

        datePicker.setOnDateSelectedListener(date -> {
            mViewModel.setDate(date);
        });


        ThreadUtils.onUI(() -> {
            mViewModel.getEditing().observe(this, enabled -> {
                boolean isInputDate = false;
                if (mViewModel.getDate().getValue() != null && mViewModel.getInputDate()!=null) {
                    isInputDate = mViewModel.getInputDate().getDateLong() == mViewModel.getDate().getValue().getDateLong();
                }
                enabled |= isInputDate;
                drugInsertView.setEnabled(enabled);
                bloodPressureView.setEnabled(enabled);
                motherWeightView.setEnabled(enabled);
                feverView.setEnabled(enabled);
                cigaretteView.setEnabled(enabled);
                alcoholView.setEnabled(enabled);
                sleepView.setEnabled(enabled);
                exerciseView.setEnabled(enabled);

                saveLog.setEnabled(enabled);
                saveLog.animate().alpha(enabled ? 1f : 0.5f);

                clearFields.setEnabled(enabled);
                clearFields.animate().alpha(enabled ? 1f : 0.5f);

                ((DrugAdapter) Objects.requireNonNull(rvDrugs.getAdapter())).setCanEdit(enabled);
            });
        },300);


        rvDrugs.setHasFixedSize(false);
        rvDrugs.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rvDrugs.setNestedScrollingEnabled(false);
        rvDrugs.setAdapter(new DrugAdapter(new DrugAdapter.ItemDiffCallBack(), new DrugAdapter.ItemClickListener() {
            @Override
            public void removeItem(long itemId, int position) {
                mViewModel.setEditingPosition(position);
                mViewModel.removeDrug(itemId);
            }

            @Override
            public void selectItemToEdit(Drug drug, int position) {
                mViewModel.setEditingPosition(position);
                mViewModel.setDrug(drug);
            }
        }));


        mViewModel.getDrug().observe(this, drug -> {
            drugInsertView.setDrug(drug);
        });

        drugInsertView.setInsertDrugListener(drug -> {
            mViewModel.addDrug(drug, success -> {
                ThreadUtils.onUI(() -> {
                    Toast.makeText(getContext(), success ? "دارو اضافه شد" : "دارو موجود است", Toast.LENGTH_SHORT).show();
                });
            });
        });

        mViewModel.getAlcohol().observe(this, alcohol -> {
            if (!mViewModel.isSaving()) {
                alcoholView.setAlcohol(alcohol);
            }
        });

        mViewModel.getCigarette().observe(this, cigarette -> {
            if (!mViewModel.isSaving()) {
                cigaretteView.setCigarette(cigarette);
            }
        });

        mViewModel.getBloodPressure().observe(this, bloodPressure -> {
            bloodPressureView.setBloodPressure(bloodPressure);
        });

        mViewModel.getDrugs().observe(this, drugs -> {
            ThreadUtils.onUI(() -> {
//                rvDrugs.setMinimumHeight(drugs.size() * 45);
                ((DrugAdapter) Objects.requireNonNull(rvDrugs.getAdapter())).submitList(drugs);
                Objects.requireNonNull(rvDrugs.getAdapter()).notifyDataSetChanged();
            });
        });

        mViewModel.getMotherWeight().observe(this, weight -> {
            motherWeightView.setWeight(weight);
        });

        mViewModel.getExerciseTime().observe(this, exerciseTime -> {
            exerciseView.setExerciseTime(exerciseTime);
        });

        mViewModel.getFever().observe(this, fever -> {
            if (!mViewModel.isSaving()) {
                feverView.setFever(fever);
            }
        });

        mViewModel.getSleepTime().observe(this, sleepTime -> {
            sleepView.setSleepTime(sleepTime);
        });


        saveLog.setOnClickListener(v -> {
            Utils.hideKeyboard((Activity) v.getContext());
            mViewModel.setSaving(true);
            mViewModel.setBloodPressure(bloodPressureView.getBloodPressure());
            mViewModel.setMotherWeight(motherWeightView.getWeight());
            mViewModel.setFever(feverView.getFever());
            mViewModel.setCigarette(cigaretteView.getCigarette());
            mViewModel.setAlcohol(alcoholView.getAlcohol());
            mViewModel.setSleepTime(sleepView.getSleepTime());
            mViewModel.setExerciseTime(exerciseView.getExerciseTime());
            String error = mViewModel.checkErrors();
            if (error == null) {
                mViewModel.saveLog(repository);
                ThreadUtils.onUI(() -> {
                    Toast.makeText(getContext(), getString(R.string.data_saved), Toast.LENGTH_SHORT).show();
                });
            } else {
                ThreadUtils.onUI(() -> {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                });
            }
            mViewModel.setSaving(false);
        });
    }
}
