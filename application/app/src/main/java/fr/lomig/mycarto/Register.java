package fr.lomig.mycarto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private TextInputLayout til_pseudo, til_email, til_password, til_password2;
    private Button mRegisterBtn;
    private TextView mLoginBtn;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        til_pseudo = findViewById(R.id.til_pseudo);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        til_password2 = findViewById(R.id.til_password2);

        mRegisterBtn = findViewById(R.id.btn_Register);
        mLoginBtn = findViewById(R.id.already);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = til_email.getEditText().getText().toString().trim();
                String password = til_password.getEditText().getText().toString().trim();
                String password2 = til_password2.getEditText().getText().toString().trim();

                //Delete all the potential previous errors
                til_pseudo.setErrorEnabled(false);
                til_email.setErrorEnabled(false);
                til_password.setErrorEnabled(false);
                til_password2.setErrorEnabled(false);

                if(TextUtils.isEmpty(email)){
                    til_email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    til_password.setError("Password is Required.");
                    return;
                }

                if(!Objects.equals(password,password2)){
                    til_password2.setError("Passwords must be the same.");
                    return;
                }

                if(password.length()<6 || password2.length()<6){
                    til_password.setError("Password must be >=6 characters.");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);

                //register the user in the firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User created.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}
