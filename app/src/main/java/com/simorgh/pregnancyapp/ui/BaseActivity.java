package com.simorgh.pregnancyapp.ui;

import android.os.Bundle;

import com.simorgh.database.Repository;
import com.simorgh.pregnancyapp.Model.AppManager;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends AppCompatActivity {
    @Inject
    protected Repository repository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getDaggerApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);

    }
}
