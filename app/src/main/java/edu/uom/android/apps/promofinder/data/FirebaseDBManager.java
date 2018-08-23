package edu.uom.android.apps.promofinder.data;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.koalap.geofirestore.GeoFire;
import com.koalap.geofirestore.GeoLocation;
import com.koalap.geofirestore.GeoQuery;
import com.koalap.geofirestore.GeoQueryDataEventListener;

import edu.uom.android.apps.promofinder.models.Promo;
import timber.log.Timber;

public class FirebaseDBManager {

    private static FirebaseDBManager mDbManager;

    //Define DB references
    private final String PROMOS = "promo";

    //Root db ref
    private FirebaseFirestore mDBReference;

    //collection references
    private CollectionReference promoRef;
    private GeoFire geoFireRef;
    private GeoQuery geoQuery;

    private FirebaseDBManager() {
        mDBReference = FirebaseFirestore.getInstance();
    }

    //Making DB manager singleton
    public static FirebaseDBManager getInstance() {
        if (null == mDbManager) {
            mDbManager = new FirebaseDBManager();
        }
        return mDbManager;
    }

    //get root db reference
    public FirebaseFirestore getRootRef() {
        return mDBReference;
    }

    public CollectionReference getPromotionRef() {
        if (promoRef == null)
            promoRef = mDBReference.collection(PROMOS);

        return promoRef;
    }

    //get promotions
    public void getPromos(double latitude, double longitude, float distance, OnSuccessCallback cb) {

        Timber.d("getPromos %s %s %s", latitude, longitude, distance);

        promoRef = getPromotionRef();

        if (geoFireRef == null)
            geoFireRef = new GeoFire(promoRef);

        if (geoQuery == null)
            geoQuery = geoFireRef.queryAtLocation(new GeoLocation(latitude, longitude), distance);
        else
            geoQuery.setLocation(new GeoLocation(latitude, longitude), distance);

        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {

            @Override
            public void onDataEntered(DocumentSnapshot documentSnapshot, GeoLocation location) {
                Promo promo = documentSnapshot.toObject(Promo.class);
                promo.setLocation(location);
                Timber.d("onDataEntered %s ", promo.toString());
                cb.onValueAdded(promo);
            }

            @Override
            public void onDataExited(DocumentSnapshot documentSnapshot) {
                Promo promo = documentSnapshot.toObject(Promo.class);
                cb.onValueRemoved(promo);
            }

            @Override
            public void onDataMoved(DocumentSnapshot documentSnapshot, GeoLocation location) {
            }

            @Override
            public void onDataChanged(DocumentSnapshot documentSnapshot, GeoLocation location) {
            }

            @Override
            public void onGeoQueryReady() {
                cb.onValueReady();
            }

            @Override
            public void onGeoQueryError(Exception error) {

            }
        });


//        mDBReference.collection(PROMOS)
//                .whereGreaterThanOrEqualTo("endDate", new Date())
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot snapshots) {
//                        List<Promo> promos1 = snapshots.toObjects(Promo.class);
//                        Timber.d("Promos %s", promos1.toString());
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
    }

    public interface OnSuccessCallback<T> {
        void onValueAdded(T data);

        void onValueReady();

        void onValueRemoved(T data);
    }

    public interface ErrorCallback {
        void onError(Exception error);
    }

}
