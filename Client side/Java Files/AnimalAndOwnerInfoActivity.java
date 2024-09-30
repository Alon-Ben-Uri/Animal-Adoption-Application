package com.example.myapplication;

import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/* Displays the information of the animal as well as the owner username and email. */
public class AnimalAndOwnerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_and_owner_info);
        Animal animal = (Animal)getIntent().getSerializableExtra("animal");
        ((TextView)findViewById(R.id.animalname)).setText(animal.getName());
        ((TextView)findViewById(R.id.animalbreed)).setText(animal.getBreed());
        ((TextView)findViewById(R.id.animalage)).setText(animal.getAge());
        ((TextView)findViewById(R.id.animalsex)).setText(animal.getSex());
        ((TextView)findViewById(R.id.animaldescription)).setText(animal.getDescription());
        ((TextView)findViewById(R.id.ownername)).setText(animal.getUsername());
        ((TextView)findViewById(R.id.owneremail)).setText(animal.getEmail());

        byte[] decodedString = Base64.decode(animal.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ((ImageView)findViewById(R.id.animalimage)).setImageBitmap(decodedByte);

    }

}