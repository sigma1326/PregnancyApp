package com.simorgh.pregnancyapp.View.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.HomeViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.ui.BaseFragment;
import com.simorgh.threadutils.ThreadUtils;
import com.simorgh.weekslider.WeekSlider;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;


public class HomeFragment extends BaseFragment {

    private UserViewModel mViewModel;
    private HomeViewModel mHomeViewModel;
    private MotionLayout motionLayout;


    @BindView(R.id.week_slider)
    WeekSlider weekSlider;

    @BindView(R.id.guildline)
    Guideline guideline;

    @BindView(R.id.card_embryo)
    FrameLayout cardEmbryo;

    @BindView(R.id.card_mother)
    FrameLayout cardMother;

    @BindView(R.id.embryo_summary)
    TextSwitcher embryoSummary;

    @BindView(R.id.tv_embryo_summary)
    TextSwitcher embryoInfo;

    @BindView(R.id.tv_mother_summary)
    TextSwitcher motherInfo;

    @BindView(R.id.img_embryo)
    ImageSwitcher embryoImage;

    @BindView(R.id.img_mother)
    ImageSwitcher motherImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private final ViewSwitcher.ViewFactory mFactory = () -> {
        TextView t = new TextView(getContext());
        t.setGravity(Gravity.START);
        t.setTypeface(Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/iransans_medium.ttf"));
        t.setTextSize(11);
        t.setTextColor(Color.parseColor("#3d3d3d"));
        return t;
    };

    private final ViewSwitcher.ViewFactory mImageFactory = () -> {
        ImageView t = new ImageView(getContext());
        t.setScaleType(ImageView.ScaleType.CENTER_CROP);
        t.setAdjustViewBounds(true);
        return t;
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (repository != null) {
            mHomeViewModel.setRepository(repository);
        }

        mViewModel.getStartWeekLabels().observe(this, pair -> {
            weekSlider.setStartTextDay(pair.first);
            weekSlider.setStartTextMonth(pair.second);
        });

        mViewModel.getEndWeekLabels().observe(this, pair -> {
            weekSlider.setEndTextDay(pair.first);
            weekSlider.setEndTextMonth(pair.second);
        });

        mViewModel.getCurrentWeekNumber().observe(this,integer -> {
            if (weekSlider != null) {
                ThreadUtils.runOnUIThread(() -> {
                    weekSlider.goToWeekNumber(integer);
                }, 500);

                if (mHomeViewModel != null) {
                    mHomeViewModel.loadWeekData(integer);
                }
            }
        });

        initFactory();

        motionLayout = (MotionLayout) view;

        ThreadUtils.runOnUIThread(() -> motionLayout.transitionToEnd(), 300);


        cardEmbryo.setOnClickListener(v -> {
            try {
                Navigation
                        .findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToArticleDetailFragment()
                                .setArticleType(1)
                                .setWeekNumber(mHomeViewModel.getWeekNumber()));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        cardMother.setOnClickListener(v -> {
            try {
                Navigation
                        .findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment)
                        .navigate(HomeFragmentDirections.actionHomeFragmentToArticleDetailFragment()
                                .setArticleType(0)
                                .setWeekNumber(mHomeViewModel.getWeekNumber()));
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        weekSlider.setStateUpdateListener((percent, weekNumber) -> {
            if (guideline != null) {
                guideline.setGuidelinePercent(1 - 0.45f * percent);
            }
            mHomeViewModel.setWeekNumber(weekNumber);
        });


        mHomeViewModel.getWeekLiveData().observe(this, week -> {
            synchronized (this) {
                if (!((TextView) embryoSummary.getCurrentView()).getText().equals(week.getEmbryoSummary())) {
                    embryoSummary.setText(week.getEmbryoSummary());
                }
                if (!((TextView) embryoInfo.getCurrentView()).getText().equals(week.getEmbryoInfo())) {
                    embryoInfo.setText(week.getEmbryoInfo());
                }
                if (!((TextView) motherInfo.getCurrentView()).getText().equals(week.getMotherInfo())) {
                    motherInfo.setText(week.getMotherInfo());
                }
                int resID = Objects.requireNonNull(getContext())
                        .getResources()
                        .getIdentifier(week.getEmbryoImageName(), "drawable", Objects.requireNonNull(getContext().getPackageName()));

                if (((ImageView) embryoImage.getCurrentView()).getDrawable() != null) {
                    if (((ImageView) embryoImage.getCurrentView()).getDrawable().getConstantState() != getResources().getDrawable(resID).getConstantState()) {
                        embryoImage.setImageResource(resID);
                    }
                } else {
                    embryoImage.setImageResource(resID);
                }

                resID = Objects.requireNonNull(getContext())
                        .getResources()
                        .getIdentifier(week.getMotherImageName(), "drawable", Objects.requireNonNull(getContext().getPackageName()));
                if (((ImageView) motherImage.getCurrentView()).getDrawable() != null) {
                    if (((ImageView) motherImage.getCurrentView()).getDrawable().getConstantState() != getResources().getDrawable(resID).getConstantState()) {
                        motherImage.setImageResource(resID);
                    }
                } else {
                    motherImage.setImageResource(resID);
                }
            }
        });
    }

    private void initFactory() {
        embryoSummary.setFactory(() -> {
            TextView t = new TextView(getContext());
            t.setGravity(Gravity.START);
            t.setTypeface(Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/iransans_medium.ttf"));
            t.setTextSize(13.3f);
            t.setTextColor(Color.parseColor("#ffffff"));
            return t;
        });
        embryoInfo.setFactory(mFactory);
        motherInfo.setFactory(mFactory);

        embryoImage.setFactory(mImageFactory);
        motherImage.setFactory(mImageFactory);
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
        weekSlider = null;
        guideline = null;
    }
}
