package edu.uom.android.apps.promofinder;

import android.app.Application;

import timber.log.Timber;

public class PromoFinderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
