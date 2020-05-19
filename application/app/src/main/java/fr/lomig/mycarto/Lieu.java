package fr.lomig.mycarto;

import com.google.android.gms.maps.model.LatLng;

public class Lieu {

    private String title;
    private LatLng corr;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getCorr() {
        return corr;
    }

    public void setCorr(LatLng corr) {
        this.corr = corr;
    }
}

