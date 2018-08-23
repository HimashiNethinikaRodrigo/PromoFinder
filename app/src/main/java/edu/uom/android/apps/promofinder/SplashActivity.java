package edu.uom.android.apps.promofinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Arrays;
import edu.uom.android.apps.promofinder.base.BaseActivity;
import edu.uom.android.apps.promofinder.util.SharedPrefsUtil;

public class SplashActivity extends BaseActivity {


    // Request Code
    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startNewActivityAndFinish(DashboardActivity.class);

//        mFirebaseAuth = FirebaseAuth.getInstance();
//
//        mAuthStateListener = firebaseAuth -> {
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//
//            if (user != null) {
//
//                startNewActivityAndFinish(DashboardActivity.class);
//
//            } else {
//
//                // User is signed out
//                SharedPrefsUtil.clearAll(this);
//                startActivityForResult(
//                        AuthUI.getInstance()
//                                .createSignInIntentBuilder()
//                                .setLogo(R.mipmap.ic_launcher)
//                                .setIsSmartLockEnabled(false)
//                                .setAvailableProviders(Arrays.asList(
//                                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER)
//                                                .build(),
//                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
//                                                .build(),
//                                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER)
//                                                .build()
//                                ))
//                                .setTheme(R.style.FirebaseTheme)
//                                .build(),
//                        RC_SIGN_IN);
//            }
//        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Checks whether the user has signed when the activity is in the foreground
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

//        if (mAuthStateListener != null) {

            // Removes the check when the activity is not in the foreground
//            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {

                finish();
            }
        }
    }

}
