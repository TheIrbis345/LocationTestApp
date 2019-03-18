package com.example.testapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener, ServerWorker.LocationSenderCallBack {
    TextView tvLat,tvLon;
    EditText tbLogin,tbPswd;
    Button btnGet;
    Button btnSend;
    LocationManager manager;

    AlertDialog waitDialog;

    Location currentLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        initViews();

        waitDialog = new DialogMaster( this ).getWaitDialog();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }

    }
    public void getCoordinates(){
        waitDialog.show();
        try {
            manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
            waitDialog.dismiss();
        }
    }

    private void initViews(){
        tvLat = findViewById( R.id.tvLat );
        tvLon = findViewById( R.id.tvLon );
        tbLogin = findViewById( R.id.tbLogin );
        tbPswd = findViewById( R.id.tbPswd );
        btnGet = findViewById( R.id.btnGet );
        btnSend = findViewById( R.id.btnSend );
        btnGet.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCoordinates();
            }
        } );

        btnSend.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLocation == null) return;
                waitDialog.show();
                ServerWorker.sendLocation( currentLocation, MainActivity.this );
            }
        } );
    }

    @Override
    public void onLocationChanged(Location location) {
        if(waitDialog.isShowing()) waitDialog.dismiss();
        currentLocation = location;
        tvLat.setText( String.valueOf( location.getLatitude() ) );
        tvLon.setText( String.valueOf( location.getLongitude() ) );
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        tvLat.setText( "No connection" );
    }


//    @Override
//    public void onSuccessLocationSend() {
//        Toast.makeText( this, "Success", Toast.LENGTH_SHORT ).show();
//    }

    @Override
    public void onSuccessLocationSend(String result) {
        waitDialog.dismiss();
        getSupportActionBar().setTitle( result );
    }

    @Override
    public void onErrorLocationSend(Exception e) {
        waitDialog.dismiss();
        Toast.makeText( this, e.toString(), Toast.LENGTH_SHORT ).show();
    }
}
