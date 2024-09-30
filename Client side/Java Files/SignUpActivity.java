package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        client = ClientManager.getClient();
    }

    /* Sends the server a sign-up request with the user's info. */
    public void finalSignUpButtonClicked(View view){
        String name, password, email;

        name = ((EditText)findViewById(R.id.signupNameText)).getText().toString();
        password = ((EditText)findViewById(R.id.signupPasswordText)).getText().toString();
        email = ((EditText)findViewById(R.id.signupEmailText)).getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "One or more missing fields.", Toast.LENGTH_SHORT).show();
        } else {
            client.sendSignupRequest("SignUp" + "|" + name + "|" + password + "|" + email);
        }

    }
}