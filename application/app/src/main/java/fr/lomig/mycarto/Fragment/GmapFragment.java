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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.lomig.mycarto.PopupAjoutLieu;
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
    private static LatLng userLatLong;
    private static boolean setzoom;
    private FragmentActivity activity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth fAuth;

    public void setZoom(Double latitude, Double longitude){
        userLatLong = new LatLng(latitude,longitude);
        setzoom = true;
    }
    public GmapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gmap, container, false);
        this.activity=this.getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        final PopupAjoutLieu lieu = new PopupAjoutLieu(activity);
        final CustomPopup infoLieu = new CustomPopup(activity);
        final GoogleMap gMap = googleMap;

        db.collection("spots")
                .whereEqualTo("proposed",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
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
                final EditText title = lieu.findViewById(R.id.entertitle);
                final EditText desc = lieu.findViewById(R.id.enterdescrip);
                final EditText cate = lieu.findViewById(R.id.entercat);

                lieu.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Object> spot = new HashMap<>();
                        spot.put("title",title.getText().toString());
                        spot.put("latitude",latLng.latitude);
                        spot.put("longitude",latLng.longitude);
                        spot.put("description",desc.getText().toString());
                        spot.put("category", cate.getText().toString());
                        spot.put("signaled",false);
                        spot.put("proposed",true);
                        spot.put("accepted","0");
                        spot.put("rating", "0");
                        spot.put("signaledby","");
                        fAuth = FirebaseAuth.getInstance();
                        String author = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                        spot.put("author",author);

                        if (title.getText().toString().matches("") || desc.getText().toString().matches("") || cate.getText().toString().matches("")) {
                            if (cate.getText().toString().matches("")){
                                cate.requestFocus();
                            }
                            if (desc.getText().toString().matches("")){
                                desc.requestFocus();
                            }
                            if (title.getText().toString().matches("")){
                                title.requestFocus();
                            }
                        }

                        else {
                            db.collection("spots").add(spot);
                            lieu.dismiss();
                        }
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

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng=marker.getPosition();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                db.collection("spots")
                        .whereEqualTo("latitude",latitude)
                        .whereEqualTo("longitude",longitude)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {

                                    for (final QueryDocumentSnapshot document : task.getResult()) {
                                        infoLieu.setTitle(document.getData().get("title").toString());
                                        infoLieu.setDescription(document.getData().get("description").toString());
                                        infoLieu.setNotepop(document.getData().get("rating").toString());
                                        infoLieu.setNoButtonText("Signaler");
                                        infoLieu.setNeutralButtonText("Retour");
                                        infoLieu.setYesButtonText("+1");

                                        infoLieu.getYesButton().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                infoLieu.setNote(Integer.parseInt(document.getData().get("rating").toString()));
                                                db.collection("spots").document(document.getId()).update("rating",infoLieu.getNote());
                                                infoLieu.dismiss();
                                            }
                                        });

                                        infoLieu.getNeutralButton().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                infoLieu.dismiss();
                                            }
                                        });

                                        infoLieu.getNoButton().setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (!document.getBoolean("signaled")) {
                                                    db.collection("spots").document(document.getId()).update("signaled",true);
                                                    db.collection("spots").document(document.getId()).update("suppress","0");
                                                    fAuth = FirebaseAuth.getInstance();
                                                    String signaler = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
                                                    db.collection("spots").document(document.getId()).update("signaledby",signaler);
                                                }
                                                infoLieu.dismiss();
                                            }
                                        });
                                        infoLieu.build();
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
                return false;
            }
        });

        locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
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
            if(setzoom){
                setzoom=false;
            }else{
                userLatLong = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLong, 15));
        }

        map.setMyLocationEnabled(true);
        map.setMapType(MAP_TYPE_SATELLITE);

    }
}
