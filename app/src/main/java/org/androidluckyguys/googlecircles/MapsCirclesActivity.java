package org.androidluckyguys.googlecircles;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

public class MapsCirclesActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String LOCATION_PREF = "locationPref";

    Context context;
    Activity activity;
    Button checkPermissionStatus;
    private GoogleMap googleMap;
    private Serializable escolas;
    private ProgressDialog dialog;
    private Circle mCircle;
    private Marker mMarker;

    //test inside
     double mLatitude = 3.183736;
            double mLongitude = 101.686679;

    //test outside
   /* double mLatitude = 3.182180;
    double mLongitude = 101.688777;*/

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_circles);

        context = MapsCirclesActivity.this;
        activity = MapsCirclesActivity.this;

        if (Build.VERSION.SDK_INT >= 23) {
            checkLocationPermission(activity, context, LOCATION_PERMISSION, LOCATION_PREF);
        }else
        {
            showMaps();
        }




    }

    private void showMaps() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void drawMarkerWithCircle(LatLng position) {
        double radiusInMeters = 200.0;  // increase decrease this distancce as per your requirements
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        CircleOptions circleOptions = new CircleOptions()
                .center(position)
                .radius(radiusInMeters)
                .fillColor(shadeColor)
                .strokeColor(strokeColor)
                .strokeWidth(8);
        mCircle = googleMap.addCircle(circleOptions);

        MarkerOptions markerOptions = new MarkerOptions().position(position);
        mMarker = googleMap.addMarker(markerOptions);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));




        // Changing map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Showing / hiding your current location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable / Disable my location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        // Bundle extra = getIntent().getBundleExtra("extra");
        //ArrayList<Escolas> objects = (ArrayList<Escolas>) extra.getSerializable("array");


        try {


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 16));

            MarkerOptions options = new MarkerOptions();

            // Setting the position of the marker

            options.position(new LatLng(mLatitude, mLongitude));

            //googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

            LatLng latLng = new LatLng(mLatitude, mLongitude);
            drawMarkerWithCircle(latLng);


            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    float[] distance = new float[2];

                        /*
                        Location.distanceBetween( mMarker.getPosition().latitude, mMarker.getPosition().longitude,
                                mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);
                                */

                    Location.distanceBetween( location.getLatitude(), location.getLongitude(),
                            mCircle.getCenter().latitude, mCircle.getCenter().longitude, distance);

                    float radius =  Float.parseFloat(mCircle.getRadius()+"");
                    float distanceInMeter = Float.parseFloat(distance[0]+"");
                    if( distanceInMeter > radius )
                    {
                        Toast.makeText(getBaseContext(), "You are Outside of the circle, Distance from center: " + distance[0] + " Radius: " + mCircle.getRadius(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Your are Inside the circle, Distance from center: " + distance[0] + " Radius: " + mCircle.getRadius() , Toast.LENGTH_LONG).show();
                    }

                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();

    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    private void checkLocationPermission(final Activity activity, final Context context, final String Permission, final String prefName) {

        PermissionUtil.checkPermission(activity,context,Permission,prefName,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onPermissionAsk() {

                        ActivityCompat.requestPermissions(MapsCirclesActivity.this,
                                new String[]{Permission},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining is permission denied previously , but app require it and then request permission

                        showToast("Permission previously Denied.");

                        ActivityCompat.requestPermissions(MapsCirclesActivity.this,
                                new String[]{Permission},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                    @Override
                    public void onPermissionDisabled() {
                        // permission check box checked and permission denied previously .
                        askUserToAllowPermissionFromSetting();

                    }
                    @Override
                    public void onPermissionGranted() {

                        showToast("Permission Granted.");
                        checkLocationPermission(activity, context, LOCATION_PERMISSION, LOCATION_PREF);

                    }
                });
    }

    private void askUserToAllowPermissionFromSetting() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Permission Required:");

        // set dialog message
        alertDialogBuilder
                .setMessage("Kindly allow Permission from App Setting, without this permission app could not show maps.")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        showToast("Permission forever Disabled.");
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

   



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the task you need to do.
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    showMaps();


                } else {


                    showToast("Permission denied,without permission can't access maps.");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}