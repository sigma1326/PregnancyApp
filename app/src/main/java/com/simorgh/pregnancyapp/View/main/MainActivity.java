package com.simorgh.pregnancyapp.View.main;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.simorgh.bottombar.BottomBar;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.View.TitleChangeListener;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity implements TitleChangeListener, BottomBar.OnItemClickListener, BottomBar.OnCircleItemClickListener, NavController.OnDestinationChangedListener {

    private TextView title;
    private BottomBar bottomBar;
    private NavController navController;
    private AppBarLayout toolbarLayout;
    private ImageButton backButton;
    private Animation animToolbarGone;
    private Animation animToolbarShow;
    private Animation animBottomGone;
    private Animation animBottomShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setBackgroundDrawable(null);



        title = findViewById(R.id.tv_app_title);
        bottomBar = findViewById(R.id.bottomBar);
        toolbarLayout = findViewById(R.id.toolbar);
        navController = Navigation.findNavController(MainActivity.this, R.id.main_nav_host_fragment);
        backButton = findViewById(R.id.img_back);

        bottomBar.setItemClickListener(this);
        bottomBar.setCircleItemClickListener(this);

        navController.addOnDestinationChangedListener(this);

        backButton.setOnClickListener(v -> {
            if (navController != null) {
                try {
                    navController.navigateUp();
                } catch (Exception e) {
                    Logger.printStackTrace(e);
                }
            }
        });

        TransitionManager.beginDelayedTransition(findViewById(R.id.main_container));

        initAnimations();

    }

    private void initAnimations() {
        animToolbarGone = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);
        animToolbarShow = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);

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

        animToolbarShow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (toolbarLayout != null) {
                    toolbarLayout.setVisibility(View.VISIBLE);
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
        animToolbarGone.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (toolbarLayout != null) {
                    toolbarLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        animBottomGone.cancel();
        animBottomShow.cancel();
        animToolbarGone.cancel();
        animToolbarShow.cancel();
        title = null;
        bottomBar = null;
        navController = null;
        toolbarLayout = null;
        backButton = null;
        animToolbarShow = null;
        animToolbarGone = null;
    }

    @Override
    public void onTitleChanged(String titleText) {
        if (title != null) {
            title.setText(titleText);
        }
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
        if (bottomBar != null && toolbarLayout != null) {
            switch (Objects.requireNonNull(controller.getCurrentDestination()).getId()) {
                case R.id.homeFragment:
                    runToolbarAnim(false);
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(4);
                    break;
                case R.id.articlesFragment:
                    runToolbarAnim(false);
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(3);
                    break;
                case R.id.logsFragment:
                    runToolbarAnim(false);
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(2);
                    break;
                case R.id.settingsFragment:
                    runToolbarAnim(false);
                    runBottomAnim(true);
                    bottomBar.setSelectedIndex(1);
                    break;
                case R.id.addLogFragment:
                    runToolbarAnim(false);
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
                case R.id.articleDetailFragment:
                    runToolbarAnim(true);
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
                case R.id.pregnancyCategoriesDetailFragment:
                    runToolbarAnim(true);
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
                case R.id.makeReportFragment:
                    runToolbarAnim(true);
                    runBottomAnim(false);
                    bottomBar.setSelectedIndex(-1);
                    break;
            }
        }
    }

    private boolean isAnimatingToolbar;
    private boolean isAnimatingBottomBar;

    private void runToolbarAnim(boolean visible) {
        if (toolbarLayout == null) {
            return;
        }
        if (visible) {
            if (toolbarLayout.getVisibility() != View.VISIBLE) {
                toolbarLayout.startAnimation(animToolbarShow);
            }
        } else {
            if (toolbarLayout.getVisibility() != View.GONE) {
                toolbarLayout.startAnimation(animToolbarGone);
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
