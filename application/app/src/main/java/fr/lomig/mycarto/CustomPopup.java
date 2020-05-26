package fr.lomig.mycarto;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.TextView;

public class CustomPopup extends Dialog {

    //fields
    private Button yesButton,neutralButton, noButton;
    private String title,description;
    private TextView titleView, descriptionView;

    // constructor
    public CustomPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.custom_popup_layout);
        this.yesButton = findViewById(R.id.yesButtonInfo);
        this.neutralButton = findViewById(R.id.neutralButtonInfo);
        this.noButton = findViewById(R.id.noButtoninfo);
        this.title = "default title";
        this.description = "default description";
        this.titleView=findViewById(R.id.titleInfo);
        this.descriptionView=findViewById(R.id.descInfo);
    }

    public Button getNoButton() {
        return noButton;
    }

    public Button getYesButton() {
        return yesButton;
    }

    public Button getNeutralButton() { return neutralButton;}

    public void setTitle(String title){ this.title=title; }

    public void setDescription(String description){ this.description=description; }

    public void build()
    {
        show();
        titleView.setText(title);
        descriptionView.setText(description);
    }
}
