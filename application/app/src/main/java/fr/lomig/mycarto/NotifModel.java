package fr.lomig.mycarto;

import android.util.Log;

public class NotifModel {

    private String type,message,spotname;

    public NotifModel() {
    }

    public NotifModel(String message, String type, String spotname) {
        this.type=type;
        this.message = message;
        this.spotname=spotname;
    }

    public String getType() { return type; }

    public String getMessage() { return message; }

    public String getSpotname() {return spotname; }

}
