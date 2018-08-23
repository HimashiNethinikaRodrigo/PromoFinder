package edu.uom.android.apps.promofinder.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import edu.uom.android.apps.promofinder.R;
import edu.uom.android.apps.promofinder.data.FirebaseDBManager;
import edu.uom.android.apps.promofinder.data.FirebaseStorageManager;
import edu.uom.android.apps.promofinder.models.Promo;
import edu.uom.android.apps.promofinder.util.DateTimeUtil;
import edu.uom.android.apps.promofinder.util.Utility;
import timber.log.Timber;

public class PromosDetailsFragment extends Fragment implements
        FirebaseDBManager.OnSuccessCallback, OnMapReadyCallback {

    @BindView(R.id.imgPromo)
    ImageView imgPromo;
    @BindView(R.id.txtShortDes)
    TextView txtShortDes;
    @BindView(R.id.txtLongDes)
    TextView txtLongDes;
    @BindView(R.id.txtExpireDate)
    TextView txtExpireDate;
    @BindView(R.id.txtCompanyName)
    TextView txtCompanyName;
    @BindView(R.id.txtCompanyDes)
    TextView txtCompanyDes;
    @BindView(R.id.mapview)
    MapView mapview;
    private GoogleMap mMap;
    private PromoFragmentInteractionListener mListener;
    private Unbinder unbinder;
    private FirebaseStorageManager storageManager;

    //this is getting from the promo list screen as a argument
    private Promo promo;


    public PromosDetailsFragment() {
        // Required empty public constructor
    }

    public static PromosDetailsFragment newInstance(Promo promo) {
        PromosDetailsFragment fragment = new PromosDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("PROMO", promo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            promo = (Promo) getArguments().getSerializable("PROMO");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        mListener.onPromoFragmentAttached();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_promo_details, container, false);
        unbinder = ButterKnife.bind(this, view);


        storageManager = FirebaseStorageManager.getInstance();

        MapsInitializer.initialize(this.getActivity());
        mapview.onCreate(savedInstanceState);
        mapview.getMapAsync(this);
        mapview.onResume();

        setPromotionData();

        return view;
    }

    private void setPromotionData() {
        if (promo.getImageUrl() != null) {
            storageManager.getPromoImage(promo.getImageUrl(), new FirebaseStorageManager.DownloadableImageUriCallback() {
                @Override
                public void onDownloadableUriSuccess(Uri uri) {
                    Utility.loadImage(getActivity(), imgPromo, uri);
                }

                @Override
                public void onDownloadableUriError(Exception e) {
                    Timber.d("onDownloadableUriError %s", e.getMessage());
                }
            });
        }

        txtShortDes.setText(promo.getShortDescription());
        txtLongDes.setText(promo.getLongDescription());
        txtExpireDate.setText(DateTimeUtil.getISODateToJustdate(new DateTime(promo.getEndDate())));
        txtCompanyName.setText(promo.getCompany().getName());
        txtCompanyDes.setText(promo.getCompany().getCompanyDescription());

    }

    @Override
    public void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof PromoFragmentInteractionListener) {
//            mListener = (PromoFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement PromoDetailsFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.d("onDetach Promo fragment");
//        mListener.onPromoFragmentDetached();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public void onValueAdded(Object data) {

    }

    @Override
    public void onValueReady() {

    }

    @Override
    public void onValueRemoved(Object data) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setOnMarkerClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        Timber.d("Location %s %s", promo.getLocation().latitude, promo.getLocation().longitude);
        placeMarker(new LatLng(promo.getLocation().latitude, promo.getLocation().longitude));
    }

    private Marker placeMarker(LatLng latLng) {
        Marker m = mMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                .position(latLng));
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(yourLocation);

        return m;
    }

    public interface PromoFragmentInteractionListener {

        void onPromoFragmentAttached();

        void onPromoFragmentDetached();

    }
}
