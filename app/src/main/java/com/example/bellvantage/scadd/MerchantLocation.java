package com.example.bellvantage.scadd;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MerchantLocation extends AppCompatActivity implements OnMapReadyCallback {
    //Google Map
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    SupportMapFragment mapFragment;

    //Merchant Details
    MerchantDetails merchantDetails;
    private String Longitude;
    private String Latitude;

    //Views
    TextView tvMerchantName, tvBuildingNumber,tvAddress, tvTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_location);

        initializeViews();

        if (getIntent().getSerializableExtra("merchantDetails")!=null){
            merchantDetails = (MerchantDetails) getIntent().getSerializableExtra("merchantDetails");
        }


        //Set Merchant Details
        tvMerchantName.setText(merchantDetails.getMerchantName());
        tvBuildingNumber.setText(""+merchantDetails.getBuildingNo());
        tvAddress.setText(merchantDetails.getAddress1()+", "+merchantDetails.getAddress2());
        tvBuildingNumber.setText(""+merchantDetails.getBuildingNo());
        tvTelephone.setText(""+merchantDetails.getContactNo1());

        if (mapFragment == null){
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    private void initializeViews() {
        tvMerchantName = (TextView) findViewById(R.id.tvMerchantName);
        tvBuildingNumber = (TextView) findViewById(R.id.tvBuildingNumber);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvTelephone = (TextView) findViewById(R.id.tvTelephone);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Longitude = merchantDetails.getLongitude();
        Latitude = merchantDetails.getLatitude();

        System.out.println("latitude: "+ Latitude+ " Longitude: "+ Longitude);

        double lon = Double.parseDouble(Longitude);
        double lat = Double.parseDouble(Latitude);

        mMap = googleMap;
        LatLng latLang = new LatLng(lat,lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, 14));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Customer Location")
                .position(latLang)));


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
