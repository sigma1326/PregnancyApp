package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.LogsViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.adapter.LogAdapter;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LogsFragment extends BaseFragment {

    private UserViewModel mUserViewModel;
    private LogsViewModel mViewModel;
    private ViewStub viewStub;
    private TextView emptyLogs;

    private RecyclerView rvLogs;
    public AtomicBoolean isInflated = new AtomicBoolean(false);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logs, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LogsViewModel.class);
        mUserViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewStub = view.findViewById(R.id.viewStub);

        mViewModel.loadLogs(repository, Objects.requireNonNull(mUserViewModel.getUser().getValue()).getPregnancyStartDate());
        mViewModel.getLogList().observe(this, logItems -> {
            if (logItems.isEmpty()) {
                if (!isInflated.get()) {
                    viewStub.setLayoutResource(R.layout.item_logs_empty);
                    viewStub.inflate();
                    isInflated.set(true);
                }
                emptyLogs = view.findViewById(R.id.tv_no_logged_data);
                emptyLogs.animate()
                        .scaleXBy(0.9f)
                        .scaleYBy(0.9f)
                        .setStartDelay(300)
                        .setInterpolator(new BounceInterpolator())
                        .setDuration(400)
                        .start();
            } else {
                if (!isInflated.get()) {
                    viewStub.setLayoutResource(R.layout.layout_logs);
                    viewStub.inflate();
                    isInflated.set(true);
                }
                rvLogs = view.findViewById(R.id.rv_logs);
                rvLogs.setHasFixedSize(false);
                rvLogs.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                rvLogs.setNestedScrollingEnabled(false);
                rvLogs.setAdapter(new LogAdapter(new LogAdapter.ItemDiffCallBack()));
                ((LogAdapter) Objects.requireNonNull(rvLogs.getAdapter()))
                        .setMinDate(Objects.requireNonNull(mUserViewModel.getUser().getValue()).getPregnancyStartDate().getCalendar());
                ((LogAdapter) Objects.requireNonNull(rvLogs.getAdapter())).submitList(logItems);

            }
        });
    }


    @Override
    public void onDestroyView() {
        viewStub = null;
        isInflated.set(false);
        super.onDestroyView();
    }
}
