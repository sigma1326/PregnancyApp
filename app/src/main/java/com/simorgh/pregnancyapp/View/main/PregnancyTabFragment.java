package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;

public class PregnancyTabFragment extends BaseFragment {

    private UserViewModel mViewModel;

    @BindView(R.id.btn_fitness)
    Button mFitnessBtn;

    @BindView(R.id.btn_general)
    Button mGeneralBtn;

    @BindView(R.id.btn_faq)
    Button mFaqBtn;

    @BindView(R.id.btn_give_birth)
    Button mGiveBirthBtn;

    @BindView(R.id.tv_current_week)
    TextView mCurrentWeek;

    @BindView(R.id.tv_remaining_days)
    TextView mRemainingDays;

    @BindView(R.id.tv_delivery_date)
    TextView mDeliveryDate;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pregnancy_tab_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);


        mFitnessBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(ArticlesFragmentDirections.actionArticlesFragmentToPregnancyCategoriesDetailFragment()
                                .setTitle(getString(R.string.nutrition))
                                .setCategoryType(1));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }

        });
        mGeneralBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(ArticlesFragmentDirections.actionArticlesFragmentToPregnancyCategoriesDetailFragment()
                                .setTitle(getString(R.string.general))
                                .setCategoryType(2));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });
        mFaqBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(ArticlesFragmentDirections.actionArticlesFragmentToPregnancyCategoriesDetailFragment()
                                .setTitle(getString(R.string.faq))
                                .setCategoryType(3));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });
        mGiveBirthBtn.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(ArticlesFragmentDirections.actionArticlesFragmentToPregnancyCategoriesDetailFragment()
                                .setTitle(getString(R.string.childbirth))
                                .setCategoryType(4));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });
        mViewModel.getRemainingDays().observe(this, s -> mRemainingDays.setText(s));
        mViewModel.getCurrentWeek().observe(this, s -> mCurrentWeek.setText(s));
        mViewModel.getDaysToDelivery().observe(this, s -> mDeliveryDate.setText(s));

    }
}
