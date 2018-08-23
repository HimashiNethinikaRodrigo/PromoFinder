package edu.uom.android.apps.promofinder.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void startNewActivity(Class<?> cls) {
        Intent it = new Intent(this, cls);
        startActivity(it);
    }

    public void startNewActivityAndFinish(Class<?> cls) {
        Intent it = new Intent(this, cls);
        startActivity(it);
        finish();
    }

}
