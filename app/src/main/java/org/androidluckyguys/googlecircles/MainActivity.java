package org.androidluckyguys.googlecircles;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

/**
 * Created by lucky on 07/03/2017.
 */

public class MainActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

    }


    public void showPolyline(View v)
    {
        Intent openPolylineActivity = new Intent (getApplicationContext(), MapsPolylineActivity.class);
        startActivity(openPolylineActivity);
    }

    public void showCircle(View v)
    {
        Intent openCircleActivity = new Intent (getApplicationContext(), MapsCirclesActivity.class);
        startActivity(openCircleActivity);
    }
}
