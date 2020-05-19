package fr.lomig.mycarto.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;

import fr.lomig.mycarto.CustomPopup;
import fr.lomig.mycarto.R;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class GmapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private static final int MAP_TYPE_SATELLITE = 2;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLatLong;
    private FragmentActivity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    public GmapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gmap, container, false);
        this.activity=this.getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final CustomPopup lieu = new CustomPopup(activity);
        final GoogleMap gMap = googleMap;

        db.collection("spots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LatLng latLngMarker = new LatLng((double)document.getData().get("latitude"), (double)document.getData().get("longitude"));

                                MarkerOptions markerOptions = new MarkerOptions();

                                markerOptions.position(latLngMarker);

                                markerOptions.title((String) document.getData().get("title"));
                                
                                gMap.addMarker(markerOptions);
                            }
                        } else {
                            Log.w(TAG,"Error getting documents.", task.getException());
                        }
                    }
                });


        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                //Creating marker
                final MarkerOptions markerOptions = new MarkerOptions();
                //Set Marker Position
                markerOptions.position(latLng);
                final EditText title = lieu.findViewById(R.id.entertitle);
                lieu.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> spot = new HashMap<>();
                        spot.put("title",title.getText().toString());
                        spot.put("latitude",latLng.latitude);
                        spot.put("longitude",latLng.longitude);
                        spot.put("description","");
                        db.collection("spots").add(spot);
                        //Set Latitude and Longitude on Marker
                        markerOptions.title(title.getText().toString());
                        //zoom camera
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        //addMarker
                        gMap.addMarker(markerOptions);
                        lieu.dismiss();
                    }
                });
                lieu.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lieu.dismiss();
                    }
                });
                lieu.build();
            }
        });


        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    userLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 15));
                } else {
                    locationManager.requestLocationUpdates(String.valueOf(locationManager.getBestProvider(new Criteria(), true)), 180000, 50, locationListener);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        map = googleMap;

        @SuppressLint("MissingPermission") Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation != null) {
            userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 15));
        }

        map.setMyLocationEnabled(true);
        map.setMapType(MAP_TYPE_SATELLITE);




    }
}
