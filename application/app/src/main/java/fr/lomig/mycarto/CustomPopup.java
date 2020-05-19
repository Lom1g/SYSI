package fr.lomig.mycarto;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;

public class CustomPopup extends Dialog {

    //fields
    private Button yesButton, noButton;

    // constructor
    public CustomPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.layout_lieu);
        this.yesButton = findViewById(R.id.yesButton);
        this.noButton = findViewById(R.id.noButton);
    }

    public Button getNoButton() {
        return noButton;
    }

    public Button getYesButton() {
        return yesButton;
    }

    public void build()
    {
        show();
    }
}
