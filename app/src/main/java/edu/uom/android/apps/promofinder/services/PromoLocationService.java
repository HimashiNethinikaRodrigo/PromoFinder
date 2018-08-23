package edu.uom.android.apps.promofinder.services;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Space;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import edu.uom.android.apps.promofinder.Constants;
import edu.uom.android.apps.promofinder.R;
import edu.uom.android.apps.promofinder.SplashActivity;
import edu.uom.android.apps.promofinder.data.FirebaseDBManager;
import edu.uom.android.apps.promofinder.models.Promo;
import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static android.support.v4.app.NotificationCompat.PRIORITY_MIN;


public class PromoLocationService extends Service implements FirebaseDBManager.OnSuccessCallback {


    private static final String CHANNEL_ID = "promo_finder";
    private static final int NOTIFICATION_ID = 1001;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 50000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private FirebaseDBManager dbManager;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Timber.d("onStartCommand");
        super.onStartCommand(intent, flags, startId);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {

        Timber.d("onCreate");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            Notification build = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(PRIORITY_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(101, build);
        }

        dbManager = FirebaseDBManager.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                Timber.d("onLocationChanged: " + location);

                Intent i = new Intent("edu.uom.android.apps.promofinder.location");
                i.putExtra(Constants.LATITUDE, location.getLatitude());
                i.putExtra(Constants.LONGITUDE, location.getLongitude());
                sendBroadcast(i);

                dbManager.getPromos(location.getLatitude(), location.getLongitude(), 1, PromoLocationService.this);
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.build();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String channelName = "Promo Finder";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                channelName, NotificationManager.IMPORTANCE_NONE);
        notificationChannel.setLightColor(Color.BLUE);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager systemService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        systemService.createNotificationChannel(notificationChannel);
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy");
        super.onDestroy();

        if (mFusedLocationClient != null && mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }

    @Override
    public void onValueAdded(Object data) {
        Promo promo = (Promo) data;
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, SplashActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = builder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent)
                .setContentText("new promo")
                .setContentText(promo.getShortDescription())
                .setPriority(PRIORITY_MIN)
                .setAutoCancel(true)
                .build();

        builder.setVibrate(new long[] {1000, 1000, 1000});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        notificationManager.notify(0, notification);

    }

    @Override
    public void onValueReady() {

    }

    @Override
    public void onValueRemoved(Object data) {

    }
}

