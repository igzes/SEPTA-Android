package org.septa.android.app;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.septa.android.app.services.apiinterfaces.SeptaServiceFactory;
import org.septa.android.app.services.apiinterfaces.model.Alerts;
import org.septa.android.app.systemstatus.SystemStatusState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainApplication extends Application implements Runnable {
    private static final int SYSTEM_STATUS_REFRESH_DELAY_SECONDS = 5 * 60;
    public static final String TAG = MainApplication.class.getSimpleName();

    private Handler refreshHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            Crashlytics.start(this);
            Log.i(TAG, "Starting Crashlytics");
        } else {
            Log.i(TAG, "Crashlytics disabled for DEBUG builds");
        }

        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(MainApplication.class.getPackage().getName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String googleApiKey = bundle.getString("com.google.android.geo.API_KEY");
            SeptaServiceFactory.setGoogleKey(googleApiKey);

            String googleBaseUrl = bundle.getString("com.google.android.geo.URL");
            SeptaServiceFactory.setGoogleApiBaseUrl(googleBaseUrl);


            String septaAmazonAwsApiKey = bundle.getString("org.septa.amazonaws.x-api-key");
            SeptaServiceFactory.setAmazonawsApiKey(septaAmazonAwsApiKey);

            String septaWebServicesBaseUrl = bundle.getString("org.septa.amazonaws.baseurl");
            SeptaServiceFactory.setSeptaWebServicesBaseUrl(septaWebServicesBaseUrl);


            SeptaServiceFactory.init();

            //SeptaServiceFactory.getFavoritesService().deleteAllFavorites(this);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        refreshHandler = new Handler();
        refreshHandler.postDelayed(this, SYSTEM_STATUS_REFRESH_DELAY_SECONDS * 1000);
    }

    @Override
    public void run() {
        SeptaServiceFactory.getAlertsService().getAlerts().enqueue(new Callback<Alerts>() {
            @Override
            public void onResponse(Call<Alerts> call, Response<Alerts> response) {
                SystemStatusState.update(response.body());
                refreshHandler.postDelayed(MainApplication.this, SYSTEM_STATUS_REFRESH_DELAY_SECONDS * 1000);
            }

            @Override
            public void onFailure(Call<Alerts> call, Throwable t) {
                t.printStackTrace();
                refreshHandler.postDelayed(MainApplication.this, SYSTEM_STATUS_REFRESH_DELAY_SECONDS * 1000);
            }
        });
    }
}
