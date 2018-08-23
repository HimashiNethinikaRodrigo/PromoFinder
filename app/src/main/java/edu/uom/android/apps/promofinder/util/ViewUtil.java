package edu.uom.android.apps.promofinder.util;

import android.app.ProgressDialog;
import android.content.Context;

import edu.uom.android.apps.promofinder.R;
import timber.log.Timber;

public class ViewUtil {
    private static ProgressDialog pd;

    public static void hideProgressBar() {
        try {
            pd.dismiss();
        } catch (Exception e) {
            Timber.d("Cannot hide the progressbar %s", e.getMessage());
        }
    }

    public static void showProgressBar(Context context, String title) {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
            pd = new ProgressDialog(context, R.style.MyDialogTheme);
            pd.setMessage(title);
            pd.setCancelable(false);
            pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pd.show();
        } catch (Exception e) {
            Timber.d("Cannot show the progressbar %s ", e.getMessage());
        }
    }

}
