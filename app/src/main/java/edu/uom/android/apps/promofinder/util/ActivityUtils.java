package edu.uom.android.apps.promofinder.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ActivityUtils {

    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentToActivityWithBackstack (@NonNull FragmentManager fragmentManager,
                                                           @NonNull Fragment fragment, int frameId, String backStackTag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.addToBackStack(backStackTag);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                                  @NonNull Fragment fragment, int frameId) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment,fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentWithRemoveStack (@NonNull FragmentManager fragmentManager,
                                                       @NonNull Fragment fragment, int frameId) {

        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment,fragment.getClass().getSimpleName());
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentToActivityWithBackstack (@NonNull FragmentManager fragmentManager,
                                                               @NonNull Fragment fragment, int frameId, String backStackTag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment,fragment.getClass().getSimpleName());
        transaction.addToBackStack(backStackTag);
        transaction.commitAllowingStateLoss();
    }
}
