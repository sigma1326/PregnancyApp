package com.simorgh.pregnancyapp.View.register;

import android.animation.LayoutTransition;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.threadutils.ThreadUtils;
import com.transitionseverywhere.ChangeText;
import com.transitionseverywhere.extra.Scale;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class RegisterActivity extends AppCompatActivity implements TitleChangeListener {

    private NavController navController;
    private Button nextButton;
    private ImageButton backButton;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        // this is a generic way of getting your root view element
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_bkg));


        ConstraintLayout parentLayout = findViewById(R.id.transition_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = new LayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
            parentLayout.setLayoutTransition(layoutTransition);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            ((ViewGroup) findViewById(R.id.transition_container)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
//            ((ViewGroup) findViewById(R.id.container)).getLayoutTransition().enableTransitionType(LayoutTransition.APPEARING);
//            ((ViewGroup) findViewById(R.id.container)).getLayoutTransition().enableTransitionType(LayoutTransition.DISAPPEARING);
//            ((ViewGroup) findViewById(R.id.container)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_APPEARING);
//            ((ViewGroup) findViewById(R.id.container)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING);
        }


        navController = Navigation.findNavController(RegisterActivity.this, R.id.register_nav_host_fragment);
        nextButton = findViewById(R.id.btn_next);
        backButton = findViewById(R.id.img_back);
        title = findViewById(R.id.tv_app_title);

        backButton.setOnClickListener(v -> {
            try {
                navController.navigateUp();
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        nextButton.setOnClickListener(v -> {
            try {
                switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                    case R.id.pregnancyStartDateFragment:
                        navController.navigate(R.id.action_pregnancyStartDateFragment_to_bloodTypeFragment);
                        break;
                    case R.id.bloodTypeFragment:
                        navController.navigate(R.id.action_bloodTypeFragment_to_motherBirthDayFragment);
                        break;
                    case R.id.motherBirthDayFragment:
                        ThreadUtils.runOnUIThread(() -> {
                            navController.navigate(R.id.action_motherBirthDayFragment_to_mainActivity);
                            RegisterActivity.this.finish();
                        });
                        break;
                }
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });

        TransitionSet set = new TransitionSet()
                .addTransition(new Scale(0.5f))
                .addTransition(new Fade())
                .addTarget(backButton)
                .setDuration(1000)
                .setInterpolator(backButton.getVisibility() == View.VISIBLE ? new LinearOutSlowInInterpolator() :
                        new FastOutLinearInInterpolator());
        TransitionManager.beginDelayedTransition(findViewById(R.id.transition_container), set);


        Transition transition = new ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT);
        transition.addTarget(TextView.class);
        transition.addTarget(Button.class);
        transition.setDuration(400);
        transition.setInterpolator(new AccelerateDecelerateInterpolator());
        TransitionManager.beginDelayedTransition(findViewById(R.id.transition_container), transition);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.pregnancyStartDateFragment:
                    backButton.setVisibility(View.INVISIBLE);
                    nextButton.setText(getString(R.string.next_stage));
                    break;
                case R.id.motherBirthDayFragment:
                    backButton.setVisibility(View.VISIBLE);
                    nextButton.setText(getString(R.string.login));
                    break;
                case R.id.bloodTypeFragment:
                    backButton.setVisibility(View.VISIBLE);
                    nextButton.setText(getString(R.string.next_stage));
                    break;
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
        navController = null;
        nextButton = null;
        backButton = null;
        title = null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public void onTitleChanged(String titleText) {
        if (title != null) {
            title.setText(titleText);
        }
    }
}
