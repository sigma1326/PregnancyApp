package com.simorgh.pregnancyapp.View.main;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.simorgh.bottombar.BottomBar;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.ui.BaseActivity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.transition.TransitionManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity implements BottomBar.OnItemClickListener
        , BottomBar.OnCircleItemClickListener, NavController.OnDestinationChangedListener {

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private NavController navController;
    private Animation animBottomGone;
    private Animation animBottomShow;

    private UserViewModel mUserViewModel;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);


        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.getUserLiveData(repository);

        getWindow().setBackgroundDrawable(null);


        navController = Navigation.findNavController(MainActivity.this, R.id.main_nav_host_fragment);

        bottomBar.setItemClickListener(this);
        bottomBar.setCircleItemClickListener(this);

        navController.addOnDestinationChangedListener(this);

        TransitionManager.beginDelayedTransition(findViewById(R.id.main_container));

        initAnimations();

    }

    private void initAnimations() {
        animBottomGone = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        animBottomShow = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        animBottomShow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (bottomBar != null) {
                    bottomBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animBottomGone.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (bottomBar != null) {
                    bottomBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        animBottomGone.cancel();
        animBottomShow.cancel();
        bottomBar = null;
        navController = null;
        unbinder.unbind();
        super.onDestroy();
    }


    @Override
    public void onClick(BottomBar.BottomItem item, boolean fromUser) {
        if (fromUser) {
            bottomBarClicked(false, item);
        }
    }

    @Override
    public void onClick(boolean fromUser) {
        if (fromUser) {
            bottomBarClicked(true, null);
        }
    }

    private void bottomBarClicked(boolean isCircle, BottomBar.BottomItem item) {
        if (isCircle || item == null) {
            try {
                switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                    case R.id.homeFragment:
                        navController.navigate(R.id.action_homeFragment_to_addLogFragment);
                        break;
                    case R.id.articlesFragment:
                        navController.navigate(R.id.action_articlesFragment_to_addLogFragment);
                        break;
                    case R.id.logsFragment:
                        navController.navigate(R.id.action_logsFragment_to_addLogFragment);
                        break;
                    case R.id.settingsFragment:
                        navController.navigate(R.id.action_settingsFragment_to_addLogFragment);
                        break;
                }
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        } else {
            try {
                switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                    case R.id.homeFragment:
                        switch (item.getIndex()) {
                            case 4:
                                break;
                            case 3:
                                navController.navigate(R.id.action_homeFragment_to_articlesFragment);
                                break;
                            case 2:
                                navController.navigate(R.id.action_homeFragment_to_logsFragment);
                                break;
                            case 1:
                                navController.navigate(R.id.action_homeFragment_to_settingsFragment);
                                break;
                        }
                        break;
                    case R.id.articlesFragment:
                        switch (item.getIndex()) {
                            case 4:
                                navController.popBackStack(R.id.homeFragment, false);
                                break;
                            case 3:
                                break;
                            case 2:
                                navController.navigate(R.id.action_articlesFragment_to_logsFragment);
                                break;
                            case 1:
                                navController.navigate(R.id.action_articlesFragment_to_settingsFragment);
                                break;
                        }
                        break;
                    case R.id.logsFragment:
                        switch (item.getIndex()) {
                            case 4:
                                navController.popBackStack(R.id.homeFragment, false);
                                break;
                            case 3:
                                navController.navigate(R.id.action_logsFragment_to_articlesFragment);
                                break;
                            case 2:
                                break;
                            case 1:
                                navController.navigate(R.id.action_logsFragment_to_settingsFragment);
                                break;
                        }
                        break;
                    case R.id.settingsFragment:
                        switch (item.getIndex()) {
                            case 4:
                                navController.popBackStack(R.id.homeFragment, false);
                                break;
                            case 3:
                                navController.navigate(R.id.action_settingsFragment_to_articlesFragment);
                                break;
                            case 2:
                                navController.navigate(R.id.action_settingsFragment_to_logsFragment);
                                break;
                            case 1:
                                break;
                        }
                        break;
                }
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        }

    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (bottomBar != null) {
            switch (Objects.requireNonNull(controller.getCurrentDestination()).getId()) {
                case R.id.homeFragment:
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(4);
                    break;
                case R.id.articlesFragment:
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(3);
                    break;
                case R.id.logsFragment:
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(2);
                    break;
                case R.id.settingsFragment:
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(1);
                    break;
                case R.id.addLogFragment:
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
                case R.id.articleDetailFragment:
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
                case R.id.pregnancyCategoriesDetailFragment:
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
                case R.id.makeReportFragment:
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
            }
        }
    }

    private void runBottomAnim(boolean visible) {
        if (bottomBar == null) {
            return;
        }
        if (visible) {
            if (bottomBar.getVisibility() != View.VISIBLE) {
                bottomBar.startAnimation(animBottomShow);
            }
        } else {
            if (bottomBar.getVisibility() != View.GONE) {
                bottomBar.startAnimation(animBottomGone);
            }
        }
    }
}
