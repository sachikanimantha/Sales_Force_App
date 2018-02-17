package com.example.bellvantage.scadd;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.OutletAdapter;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.DeliveryPath;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MyOutletListActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    //Toolbar
    Toolbar mToolBar;

    //Search
    EditText etSearchOutlet;
    OutletAdapter outletAdapter;
    LinearLayout llSearch;

    //Google Map
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    SupportMapFragment mapFragment;
   // private GoogleApiClient client;

    TextView tvGetOutlet,tvPath,tvMerchantSelect;

    //Path
    ArrayList<DeliveryPath> pathArrayList = new ArrayList<>();
    int pathCode;
    ArrayList<String> pathItems=new ArrayList<>();
    SpinnerDialog pathSpinnerDialog;
    SyncManager syncManager;


    ArrayList<MerchantDetails> merchantArrayList = new ArrayList<>();

    RecyclerView rvOutlet;

    // AlertDialog
    AlertDialog dialog;
    AlertDialog.Builder mBuilder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_outlet_list);

        initializeViews();

        llSearch.setVisibility(View.GONE);

        //path Spinner
        tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathSpinnerDialog.showSpinerDialog();
            }
        });

        //set data to spinners
        syncManager = new SyncManager(getApplicationContext());
        pathArrayList=syncManager.getAllPath();

        loadPath(pathArrayList);

        tvGetOutlet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMerchantList();
            }
        });




        //Search Outlet
        etSearchOutlet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString().toLowerCase();
                ArrayList<MerchantDetails> newMerchantArrayList = new ArrayList<>();
                for(MerchantDetails merchantDetails:merchantArrayList){
                    String merchantName = merchantDetails.getMerchantName().toLowerCase();
                    if(merchantName.contains(newText)){
                        newMerchantArrayList.add(merchantDetails);
                    }
                }
                outletAdapter.setFilter(newMerchantArrayList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void loadPath(final ArrayList<DeliveryPath> pathArrayList) {
        pathItems.clear();
        for (int i = 0; i < pathArrayList.size(); i++) {
            pathItems.add(pathArrayList.get(i).getPathName());
        }
        pathSpinnerDialog=new SpinnerDialog(MyOutletListActivity.this,pathItems,"Select or Search Path",R.style.DialogAnimations_SmileWindow);

        pathSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvPath.setText(item);

                for (int  i = 0 ;i<pathArrayList.size();i++){

                    if (pathArrayList.get(i).getPathName().equals(item)){
                        pathCode = pathArrayList.get(i).getDeliveryPathId();
                    }
                }

            }
        });
    }

    private void initializeViews() {

        //Toolbar
        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setTitle("My Outlet List");

        //TextViews
        tvGetOutlet = (TextView)findViewById(R.id.tvGetOutlet);
        tvPath= (TextView)findViewById(R.id.tvPaths);

        //RecycleView
        rvOutlet = (RecyclerView)findViewById(R.id.rvOutlet);

        //EditText
        etSearchOutlet = (EditText) findViewById(R.id.etSearchOutlet);

        //LinearLayouts
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
    }

    private void loadMerchantList(){

        String pat = tvPath.getText().toString();

        if(pat.equals("Select or Search Path")){
            Toast.makeText(getApplicationContext(), "You haven't selected a path.", Toast.LENGTH_SHORT).show();
            return;
        }else{

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        rvOutlet.setLayoutManager(layoutManager);
        rvOutlet.setHasFixedSize(true);
        SyncManager syncManager = new SyncManager(getApplicationContext());
        merchantArrayList = syncManager.getMerchantListByPathID(pathCode);
        outletAdapter = new OutletAdapter(MyOutletListActivity.this,merchantArrayList);
        rvOutlet.setAdapter(outletAdapter);

        if(merchantArrayList.size()>0){
            llSearch.setVisibility(View.VISIBLE);
        }
    }

    //View Location of the outlet
    public void getQty(){


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyOutletListActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_for_outlet_map,null);

        if (mapFragment == null){
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        TextView tvButton;
        tvButton = (TextView)mView.findViewById(R.id.tvButton);
        Fragment fragment = (getFragmentManager()
                .findFragmentById(R.id.map));

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();

            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng latlang = new LatLng(6.9210809,79.8666594);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, 15));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Customer Location")
                .position(latlang)));
        //System.out.println("Longitude = "+longitude);
        //System.out.println("Latitude = "+latitude);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStop() {
        System.out.println("Stop Map fragment");
        super.onStop();
    }

}
