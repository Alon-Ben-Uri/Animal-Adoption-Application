package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartingActivity extends AppCompatActivity {

    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_starting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /* Initializes the client. */
        client = new Client();
        client.start();
        ClientManager.setClient(client);
    }

    /* Enters user as a guest. */
    public void guestButtonClicked(View view){
        client.setGuest();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    /* Sends the user to a sign-in screen. */
    public void signInButtonClicked(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /* Sends the user to a sign-up screen. */
    public void signUpButtonClicked(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

}