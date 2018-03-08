package com.example.aniru.bustracker;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentloation;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    double latitude,longitude;
    LatLng currentlocation;
    SupportMapFragment mapFragment;
    String busid;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    DatabaseReference reference1,reference2;
    MarkerOptions wait_marker,bus_marker;
    Marker wait,bus;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle=getIntent().getExtras();
         busid=bundle.getString("busid");
        reference1= FirebaseDatabase.getInstance().getReference(busid).child("LATITUDE");
        reference2= FirebaseDatabase.getInstance().getReference(busid).child("LONGITUDE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.setRetainInstance(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mLocationRequest= new LocationRequest();
        mLocationRequest.setInterval(10);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient.connect();
        mapFragment.getMapAsync(this);
    }

    protected void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }
    protected void onStop()
    {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
        }
        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mCurrentloation=mLastLocation;
            latitude=mCurrentloation.getLatitude();
            longitude=mCurrentloation.getLongitude();
            currentlocation = new LatLng(latitude,longitude);
            //reference1.child("LATITUDE").setValue(latitude);
            //reference1.child("LONGITUDE").setValue(longitude);
            //mMap.addMarker(new MarkerOptions().position(currentlocation).title("Marker in CURRRENT LOCATION"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));

        }
            startLocationUpdates();
    }
    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {

        mCurrentloation = location;
        latitude=mCurrentloation.getLatitude();
        longitude=mCurrentloation.getLongitude();
        currentlocation = new LatLng(latitude,longitude);
        reference1.setValue(latitude);
        reference2.setValue(longitude);

        //mMap.addMarker(new MarkerOptions().position(currentlocation).title("Marker in CURRRENT LOCATION"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));



    }

}
