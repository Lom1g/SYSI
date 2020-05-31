package fr.lomig.mycarto;

import android.util.Log;

public class NotifModel {

    private String title,message,spotname;

    public NotifModel() {
    }

    public NotifModel(String message, String title, String spotname) {
        this.title=title;
        this.message = message;
        this.spotname=spotname;
    }

    public String getTitle() { return title; }

    public String getMessage() { return message; }

    public String getSpotname() {return spotname; }

}
