package fr.lomig.mycarto;

import com.google.android.gms.maps.model.LatLng;

public class Lieu {

    private String title;
    private LatLng position;

    public Lieu() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}