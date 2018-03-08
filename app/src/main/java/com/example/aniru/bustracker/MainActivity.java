package com.example.aniru.bustracker;

import android.app.Application;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.FirebaseApp;
import com.firebase.client.Firebase;




import java.util.Map;

import static android.content.Intent.ACTION_VIEW;


public class MainActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private GoogleApiClient mGoogleApiClient;
    private Firebase mref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        final Button authuser=(Button) findViewById(R.id.provider_logbtn);
        final Button waituser= (Button) findViewById(R.id.user_trackbtn);
        final EditText username= (EditText) findViewById(R.id.provider_userid);
        final EditText password=(EditText) findViewById(R.id.provider_password);
        final EditText track_busid= (EditText) findViewById(R.id.user_busid);
        final EditText login_busid = (EditText) findViewById(R.id.busid_login);
        FirebaseApp.initializeApp(this);

        waituser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busid=track_busid.getText().toString();
                Intent intent=new Intent(MainActivity.this,TrackingActivity.class);
                intent.putExtra("busid",busid);
                startActivity(intent);
            }
        });
        authuser.setOnClickListener(new View.OnClickListener(){
            String user1, pass1, user2, pass2, busid;
            @Override
            public void onClick(View v){

                  busid= login_busid.getText().toString();
                  user1= username.getText().toString();
                  pass1= password.getText().toString();

                DatabaseReference reference1= FirebaseDatabase.getInstance().getReference(busid);
                /*Intent exp=new Intent(MainActivity.this,debugactivity.class);
                 startActivity(exp);*/
                mref =new Firebase ("https://bus-tracker-project-f0928.firebaseio.com/"+ busid);

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                          user2=dataSnapshot.child("USERNAME").getValue().toString();
                          pass2=dataSnapshot.child("PASSWORD").getValue().toString();
                        if (user1.equals(user2) && pass1.equals(pass2))
                        {
                            Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                            intent.putExtra("busid",busid);
                            startActivity(intent);
                        }
                        else
                        {
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Intent exp=new Intent(MainActivity.this,debugactivity.class);
                        startActivity(exp);
                    }
                });
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

