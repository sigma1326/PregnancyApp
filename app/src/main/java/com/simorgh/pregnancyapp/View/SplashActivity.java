package com.simorgh.pregnancyapp.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.simorgh.database.Repository;
import com.simorgh.pregnancyapp.View.main.MainActivity;
import com.simorgh.pregnancyapp.View.register.RegisterActivity;
import com.simorgh.pregnancyapp.ui.BaseActivity;

public class SplashActivity extends BaseActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        repository.getUserSingle()
                .compose(Repository.applySingle())
                .subscribe((user, throwable) -> {
                    if (throwable == null) {
                        if (user != null) {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(this, RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Intent intent = new Intent(this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
