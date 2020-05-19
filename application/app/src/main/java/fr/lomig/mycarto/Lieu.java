package fr.lomig.mycarto;

import com.google.android.gms.maps.model.LatLng;

public class Lieu {

    private String title;
    private LatLng latlong;

    public LatLng getLatlong() {
        return latlong;
    }

    public void setLatlong(LatLng latlong) {
        this.latlong = latlong;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
