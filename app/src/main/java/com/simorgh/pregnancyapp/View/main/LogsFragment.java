package com.simorgh.pregnancyapp.View.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.simorgh.database.Date;
import com.simorgh.logger.Logger;
import com.simorgh.pregnancyapp.R;
import com.simorgh.pregnancyapp.ViewModel.main.LogsViewModel;
import com.simorgh.pregnancyapp.ViewModel.main.UserViewModel;
import com.simorgh.pregnancyapp.adapter.LogAdapter;
import com.simorgh.pregnancyapp.adapter.LogItem;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LogsFragment extends BaseFragment {

    private UserViewModel mUserViewModel;
    private LogsViewModel mViewModel;
    private ViewStub viewStub;
    private TextView emptyLogs;

    private RecyclerView rvLogs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logs_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LogsViewModel.class);
        mUserViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(UserViewModel.class);
    }

    private Disposable disposable;

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewStub = view.findViewById(R.id.viewStub);


        disposable = repository
                .getLoggedDates()
                .observeOn(Schedulers.io())
                .map(dates -> {
                    List<LogItem> logItemList = new ArrayList<>();
                    for (Date d : dates) {
                        LogItem logItem = new LogItem(d, Objects.requireNonNull(mUserViewModel.getUser().getValue()).getPregnancyStartDate());
                        logItemList.add(logItem);
                    }
                    return logItemList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(logItems -> {
                    if (logItems.isEmpty()) {
                        viewStub.setLayoutResource(R.layout.logs_empty_layout);
                        viewStub.inflate();
                        emptyLogs = view.findViewById(R.id.tv_no_logged_data);
                        emptyLogs.animate()
                                .scaleXBy(0.9f)
                                .scaleYBy(0.9f)
                                .setStartDelay(300)
                                .setInterpolator(new BounceInterpolator())
                                .setDuration(400)
                                .start();
                    } else {
                        viewStub.setLayoutResource(R.layout.logs_layout);
                        viewStub.inflate();
                        rvLogs = view.findViewById(R.id.rv_logs);
                        rvLogs.setHasFixedSize(false);
                        rvLogs.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        rvLogs.setNestedScrollingEnabled(false);
                        rvLogs.setAdapter(new LogAdapter(new LogAdapter.ItemDiffCallBack()));
                        ((LogAdapter) Objects.requireNonNull(rvLogs.getAdapter()))
                                .setMinDate(Objects.requireNonNull(mUserViewModel.getUser().getValue()).getPregnancyStartDate().getCalendar());
                        ((LogAdapter) Objects.requireNonNull(rvLogs.getAdapter())).submitList(logItems);

                    }
                }, Logger::printStackTrace);
    }


    @Override
    public void onDestroyView() {
        viewStub = null;
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
        super.onDestroyView();
    }
}
