package edu.uom.android.apps.promofinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.uom.android.apps.promofinder.adapters.PromoAdapter;
import edu.uom.android.apps.promofinder.base.BaseActivity;
import edu.uom.android.apps.promofinder.data.FirebaseDBManager;
import edu.uom.android.apps.promofinder.fragments.PromosDetailsFragment;
import edu.uom.android.apps.promofinder.fragments.PromosFragment;
import edu.uom.android.apps.promofinder.models.Promo;
import edu.uom.android.apps.promofinder.receiver.LocationReceiver;
import edu.uom.android.apps.promofinder.services.PromoLocationService;
import edu.uom.android.apps.promofinder.util.ActivityUtils;
import edu.uom.android.apps.promofinder.util.PermissionUtil;
import edu.uom.android.apps.promofinder.util.ViewUtil;
import timber.log.Timber;


public class DashboardActivity extends BaseActivity implements
        PromosFragment.PromoFragmentInteractionListener {


    protected static final int LOCATION_PERMISSION_REQUEST = 1002;
    private static final String FRAGMENT_PROMO = "fragment_promo";

    @BindView(R.id.btnFilter)
    ImageView btnFilter;

    private String currentFragment;
    private PromosFragment promosFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        promosFragment = PromosFragment.newInstance();
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), promosFragment, R.id.mainContainer);

        getLocationPermission();

    }

    @OnClick(R.id.btnFilter)
    void onClickBtnFilter() {
        if (currentFragment == FRAGMENT_PROMO) {
            promosFragment.showFilterView();
        }
    }

    protected boolean getLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.hasPermissions(this,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                return true;

            } else {
                PermissionUtil.checkAndRequestPermissions(this, LOCATION_PERMISSION_REQUEST,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getLocationPermission())
            startLocationService();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                startLocationService();
            }
        }
    }

    private void startLocationService() {
        Intent intent1 = new Intent(this, PromoLocationService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent1);
        } else {
            startService(intent1);
        }


    }

    @Override
    public void onPromoFragmentAttached() {
        currentFragment = FRAGMENT_PROMO;
    }

    @Override
    public void onPromoFragmentDetached() {

    }

    @Override
    public void onPromoDetailsClick(Promo promo) {
        ActivityUtils.addFragmentToActivityWithBackstack(getSupportFragmentManager(),
                PromosDetailsFragment.newInstance(promo),R.id.mainContainer,PromosFragment.class.getSimpleName());
    }
}
