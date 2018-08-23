package edu.uom.android.apps.promofinder.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import timber.log.Timber;

/**
 * manages the storage needs of the app using firebase
 */
public class FirebaseStorageManager {

    private final String PROMO_IMAGES = "promo_images";


    private static FirebaseStorageManager mFirebaseStorageManager;

    private StorageReference storageReference;

    private FirebaseStorageManager() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static FirebaseStorageManager getInstance() {
        if (null == mFirebaseStorageManager) {
            mFirebaseStorageManager = new FirebaseStorageManager();
        }
        return mFirebaseStorageManager;
    }

    //get promo image Uri from callback
    public void getPromoImage(String photoUrl, DownloadableImageUriCallback cb) {
        storageReference.child(PROMO_IMAGES).
                child("86dioVeAgkxGshQTWZyD.jpeg").getDownloadUrl().addOnCompleteListener(task -> {
            if (task.getException() != null)
                cb.onDownloadableUriError(task.getException());
            else
                cb.onDownloadableUriSuccess(task.getResult());
        }).addOnFailureListener(e -> {
            cb.onDownloadableUriError(e);
        });
    }

    //upload menu item image
    public void uploadMenuItemImage(Context context, String menuImageUrl, Uri uri, UploadMenuItemImageCallback cb) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        storageReference.child(PROMO_IMAGES).child(menuImageUrl).putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    cb.onImageUploadSuccess(taskSnapshot);
                })
                .addOnFailureListener(exception -> {
                    progressDialog.dismiss();
                    cb.onImageUploadError(exception);
                });

    }

    public interface UploadMenuItemImageCallback {
        void onImageUploadSuccess(UploadTask.TaskSnapshot snapshot);

        void onImageUploadError(Exception e);
    }



    public interface DownloadableImageUriCallback {
        void onDownloadableUriSuccess(Uri uri);

        void onDownloadableUriError(Exception e);

    }
}
