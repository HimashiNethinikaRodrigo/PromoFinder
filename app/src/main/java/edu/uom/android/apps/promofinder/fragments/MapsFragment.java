package edu.uom.android.apps.promofinder.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import edu.uom.android.apps.promofinder.R;
import timber.log.Timber;

import static android.os.Build.VERSION_CODES.M;

public class MapsFragment extends android.support.v4.app.Fragment
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback {

    private static final String TAG = MapsFragment.class.getSimpleName();

    @BindView(R.id.map)
    MapView mapView;

    GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.foundRestaurantButton)
    Button mRestaurantFoundButton;

    private MapsFragmentInteractionListener mListener;
    private Unbinder unbinder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_maps, container, false);

        unbinder = ButterKnife.bind(this, inflate);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        //check if location service is enable
        //to get the current location of the user
        isLocationServiceEnabled(getActivity());

        // Initialize GoogleMaps
        initializeGoogleMaps();

        // Create GoogleApiClient
        buildGoogleApiClient();

        mRestaurantFoundButton.setOnClickListener((View view) -> {
            LatLng mTargetLocation = mMap.getCameraPosition().target;


        });

        return inflate;
    }

    @OnClick(R.id.layout_main)
    void mainViewClick(){
        Timber.d("mainViewClick");
    }

    @Override
    public void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    // Builds a GoogleApiClient. Uses {@code #addApi} to request the LocationServices API.
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getInitialCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    // Get initial known location
    private void getInitialCurrentLocation() {
        Log.d(TAG, "getInitialCurrentLocation()");
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation != null) {
                Timber.d(TAG, "Initial Location. " + "Long: " + mCurrentLocation.getLongitude() +
                        " | Lat: " + mCurrentLocation.getLatitude());

                // Zoom into current location
                LatLng coordinate = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                mMap.animateCamera(yourLocation);

            } else {
                Timber.d(TAG, "No location retrieved yet");
            }
        }
    }

    // Initialize GoogleMaps
    private void initializeGoogleMaps() {
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(MapsFragment.this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapsFragmentInteractionListener) {
            mListener = (MapsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapsFragmentInteractionListener");
        }
    }

    // Checks if Location Services are enabled
    private void isLocationServiceEnabled(final Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ignored) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ignored) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings),
                    (DialogInterface paramDialogInterface, int paramInt) -> {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(myIntent);
                    });
            dialog.setNegativeButton(context.getString(R.string.cancel), (DialogInterface paramDialogInterface, int paramInt) -> {

            });
            dialog.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface MapsFragmentInteractionListener {
        void onSelectLocationOnMap(Bundle bundle);
    }

}



