package fr.lomig.mycarto;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NotifPopup extends Dialog {

    //fields
    private Button yesButton, noButton;
    private String title,description,yesText,noText;
    private TextView titleView, descriptionView;
    private EditText message;

    // constructor
    public NotifPopup(Activity activity)
    {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.popup_notif);
        this.yesButton = findViewById(R.id.yesButtonInfo);
        this.noButton = findViewById(R.id.noButtoninfo);
        this.yesText = "Yes";
        this.noText = "No";
        this.title = "default title";
        this.description = "default description";
        this.titleView=findViewById(R.id.titleInfo);
        this.descriptionView=findViewById(R.id.descInfo);
        this.message= findViewById(R.id.message);
    }

    public Button getNoButton() {
        return noButton;
    }

    public Button getYesButton() { return yesButton; }

    public void setYesButtonText(String yesText){this.yesButton.setText(yesText);}

    public void setNoButtonText(String noText){this.noButton.setText(noText);}

    public void setTitle(String title){ this.title=title; }

    public void setDescription(String description){ this.description=description; }

    public EditText getMessage() { return message;}

    public void build()
    {
        show();
        titleView.setText(title);
        descriptionView.setText(description);
    }
}
