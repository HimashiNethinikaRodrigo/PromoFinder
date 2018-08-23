package edu.uom.android.apps.promofinder.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import org.joda.time.DateTime;

import timber.log.Timber;

public class Utility {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isServiceRunning(Class<?> serviceClass,Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
            }
            Timber.d("Service Running %s",serviceClass.getName());
        }
        return false;
    }

    public static void loadImage(Context context, ImageView imageView, Uri uri){
        RequestOptions options=new RequestOptions().signature(new ObjectKey(new DateTime().getMillisOfSecond()));

        Glide.with(context)
                .load(uri)
                .apply(options)
                .into(imageView);
    }

}
