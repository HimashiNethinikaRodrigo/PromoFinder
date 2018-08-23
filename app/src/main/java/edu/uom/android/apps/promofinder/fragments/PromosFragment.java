package edu.uom.android.apps.promofinder.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import edu.uom.android.apps.promofinder.R;
import edu.uom.android.apps.promofinder.adapters.PromoAdapter;
import edu.uom.android.apps.promofinder.data.FirebaseDBManager;
import edu.uom.android.apps.promofinder.models.Promo;
import edu.uom.android.apps.promofinder.receiver.LocationReceiver;
import edu.uom.android.apps.promofinder.util.ActivityUtils;
import edu.uom.android.apps.promofinder.util.ViewUtil;
import timber.log.Timber;

import static edu.uom.android.apps.promofinder.Constants.LATITUDE;
import static edu.uom.android.apps.promofinder.Constants.LONGITUDE;

public class PromosFragment extends Fragment implements PromoAdapter.ItemListItemClickListener, FirebaseDBManager.OnSuccessCallback {


    @BindView(R.id.rvPromos)
    RecyclerView rvPromos;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.radiusSeekBar)
    SeekBar radiusSeekBar;
    @BindView(R.id.llRadiusSeekBar)
    LinearLayout llRadiusSeekBar;
    @BindView(R.id.txtRadius)
    TextView txtRadius;

    private IntentFilter locationFilter = new IntentFilter("edu.uom.android.apps.promofinder.location");
    private LocationReceiver locationReceiver;
    private FusedLocationProviderClient mFusedLocationClient;
    private PromoFragmentInteractionListener mListener;
    private Unbinder unbinder;
    private boolean isShowFilterView;
    private FirebaseDBManager dbManager;
    private ArrayList<Promo> promoList;
    private PromoAdapter promoAdapter;
    private double currentLatitude = 0.0;
    private double currnetLongitude = 0.0;

    public PromosFragment() {
        // Required empty public constructor
    }

    public static PromosFragment newInstance() {
        PromosFragment fragment = new PromosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mListener.onPromoFragmentAttached();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo, container, false);
        unbinder = ButterKnife.bind(this, view);

        dbManager = FirebaseDBManager.getInstance();
        promoList = new ArrayList<>();

        rvPromos.setLayoutManager(new LinearLayoutManager(getContext()));
        promoAdapter = new PromoAdapter(getContext(), promoList, this);
        rvPromos.setAdapter(promoAdapter);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        txtRadius.setText(String.format("%d Km", radiusSeekBar.getProgress()));

        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtRadius.setText(String.format("%d Km", progress));
                if (currentLatitude != 0.0 && currnetLongitude != 0.0)
                    getPromotionsToCurrentLocation(currentLatitude, currnetLongitude);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return view;
    }

    private void getPromotionsToCurrentLocation(double latitude, double longitude) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            promoList.clear();
            dbManager.getPromos(latitude, longitude, radiusSeekBar.getProgress(), this);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        ViewUtil.showProgressBar(getContext(), "loading promotions");

        locationReceiver = new LocationReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ViewUtil.hideProgressBar();

                if (currentLatitude == 0.0 && currnetLongitude == 0.0) {
                    currentLatitude = intent.getDoubleExtra(LATITUDE, 0.0);
                    currnetLongitude = intent.getDoubleExtra(LONGITUDE, 0.0);
                    getPromotionsToCurrentLocation(currentLatitude, currnetLongitude);
                }
            }
        };

        getContext().registerReceiver(locationReceiver, locationFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        getContext().unregisterReceiver(locationReceiver);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PromoFragmentInteractionListener) {
            mListener = (PromoFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PromoFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach Promo fragment");
        mListener.onPromoFragmentDetached();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showFilterView() {
        if (isShowFilterView) {
            llRadiusSeekBar.setVisibility(View.GONE);
            isShowFilterView = !isShowFilterView;
        } else {
            llRadiusSeekBar.setVisibility(View.VISIBLE);
            isShowFilterView = !isShowFilterView;
        }
    }

    @Override
    public void onValueAdded(Object data) {
        if (!promoList.contains((Promo) data)) {
            promoList.add((Promo) data);
            promoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onValueReady() {
        progressBar.setVisibility(View.GONE);
        ViewUtil.hideProgressBar();
    }

    @Override
    public void onValueRemoved(Object data) {
//        promoList.remove((Promo) data);
//        promoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickItem(Promo promo) {
        mListener.onPromoDetailsClick(promo);
    }


    public interface PromoFragmentInteractionListener {

        void onPromoFragmentAttached();

        void onPromoFragmentDetached();

        void onPromoDetailsClick(Promo promo);

    }
}
