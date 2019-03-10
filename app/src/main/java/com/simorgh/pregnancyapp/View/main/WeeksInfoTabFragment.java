package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.database.callback.ParagraphsCallBack;
import com.simorgh.database.model.Paragraph;
import com.simorgh.database.model.Week;
import com.simorgh.fluidslider.FluidSlider;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.WeeksInfoTabViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.pregnancyapp.ui.EmbryoWeekInfoView;
import com.simorgh.pregnancyapp.ui.MotherWeekInfoView;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;

public class WeeksInfoTabFragment extends BaseFragment {

    private UserViewModel mViewModel;
    private WeeksInfoTabViewModel mLocalViewModel;

    @BindView(R.id.embryoWeekInfoView)
    EmbryoWeekInfoView embryoWeekInfoView;


    @BindView(R.id.motherWeekInfoView)
    MotherWeekInfoView motherWeekInfoView;

    @BindView(R.id.fluid_week_slider)
    FluidSlider fluidSlider;

    @BindView(R.id.btnRight)
    ImageButton btnRight;

    @BindView(R.id.btnLeft)
    ImageButton btnLeft;


    private int last = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.weeks_info_tab_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
        mLocalViewModel = ViewModelProviders.of(this).get(WeeksInfoTabViewModel.class);
        mLocalViewModel.setRepository(repository);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);


        mViewModel.getUser().observe(Objects.requireNonNull(getActivity()), user -> {
            if (user != null && motherWeekInfoView != null) {
                if (mLocalViewModel != null) {
                    Calendar nowC = Calendar.getInstance();
                    float diffDays = CalendarTool.getDaysFromDiff(nowC, user.getPregnancyStartDate().getCalendar());
                    int diffWeek = (int) (diffDays / 7) + 1;
                    mLocalViewModel.syncWeekNumber(diffWeek);
                    last = diffWeek - 1;
                    fluidSlider.setCurrentPosition(last);
                }
            }
        });

        mLocalViewModel.getWeekLiveData().observe(this, week -> {
            embryoWeekInfoView.setTitle(String.format("اطلاعات مربوط به نوزاد در هفته %d ام", week.getWeekNumber()));

            repository.getParagraphs(week.getEmbryoArticleID(), new ParagraphsCallBack() {
                @Override
                public void onSuccess(List<Paragraph> paragraphs) {
                    if (!paragraphs.isEmpty() && embryoWeekInfoView != null) {
                        embryoWeekInfoView.setSummary(paragraphs.get(0).getContent());
                    }
                }

                @Override
                public void onFailed(String error) {

                }
            });


            motherWeekInfoView.setTitle(String.format("اطلاعات مربوط به مادر در هفته %d ام", week.getWeekNumber()));
            repository.getParagraphs(week.getMotherArticleID(), new ParagraphsCallBack() {
                @Override
                public void onSuccess(List<Paragraph> paragraphs) {
                    if (!paragraphs.isEmpty()) {
                        motherWeekInfoView.setSummary(paragraphs.get(0).getContent());
                    }
                }

                @Override
                public void onFailed(String error) {

                }
            });
        });

        btnLeft.setOnClickListener(v -> {
            try {
                int current = last;
                if (current + 1 < 40) {
                    fluidSlider.setCurrentPosition(++last);
                }
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        btnRight.setOnClickListener(v -> {
            try {
                int current = fluidSlider.getPosition();
                if (current - 1 >= 0) {
                    fluidSlider.setCurrentPosition(--last);
                }
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        motherWeekInfoView.setClickedListener(view1 -> {
            try {
                Week week = mLocalViewModel.getWeekLiveData().getValue();
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(ArticlesFragmentDirections.actionArticlesFragmentToArticleDetailFragment()
                                .setWeekNumber(Objects.requireNonNull(week).getWeekNumber())
                                .setArticleType(0));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        embryoWeekInfoView.setClickedListener(view1 -> {
            try {
                Week week = mLocalViewModel.getWeekLiveData().getValue();
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(ArticlesFragmentDirections.actionArticlesFragmentToArticleDetailFragment()
                                .setWeekNumber(Objects.requireNonNull(week).getWeekNumber())
                                .setArticleType(1));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        fluidSlider.setFluidSliderListener(new FluidSlider.FluidSliderListener() {
            @Override
            public void invokePosition(int position, boolean fromUser) {
                if (fromUser) {
                    last = position - 1;
                }
                mLocalViewModel.syncWeekNumber(last + 1);
            }

            @Override
            public void invokeBeginTracking() {

            }

            @Override
            public void invokeEndTracking() {

            }
        });
    }
}

