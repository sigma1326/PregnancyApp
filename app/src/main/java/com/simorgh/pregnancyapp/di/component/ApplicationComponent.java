package com.simorgh.pregnancyapp.di.component;

import com.simorgh.pregnancyapp.di.module.ApplicationModule;
import com.simorgh.pregnancyapp.di.module.DataBaseModule;
import com.simorgh.pregnancyapp.ui.BaseActivity;
import com.simorgh.pregnancyapp.ui.BaseFragment;

import dagger.Component;

@Component(modules = {ApplicationModule.class, DataBaseModule.class})
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);
//    void inject(ArticleDetailFragment fragment);

    void inject(BaseFragment baseFragment);

}
