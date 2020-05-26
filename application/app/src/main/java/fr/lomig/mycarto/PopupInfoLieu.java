package fr.lomig.mycarto;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class PopupInfoLieu extends Dialog {

    //fields
    private Button yesButton, noButton;
    private String title,description;
    private Integer note;
    private TextView titleView, descriptionView;

    // constructor
    public PopupInfoLieu(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.popup_info_lieu);
        this.yesButton = findViewById(R.id.yesButtonInfo);
        this.noButton = findViewById(R.id.noButtonInfo);
        this.title = "default title";
        this.description = "default description";
        this.note = 0;
        this.titleView=findViewById(R.id.titleInfo);
        this.descriptionView=findViewById(R.id.descInfo);
    }

    public Button getNoButton() {
        return noButton;
    }

    public Button getYesButton() {
        return yesButton;
    }

    public void setTitle(String title){ this.title=title; }

    public void setDescription(String description){ this.description=description; }

    public void setNote(int note){this.note = note + 1;}

    public int getNote(){return note;}

    public void build()
    {
        show();
        titleView.setText(title);
        descriptionView.setText(description);
    }
}
