package com.example.myapplication;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/* HomeActivity lets the client view animals and filter them via multiple categories, as well as set an
*  animal for adoption and view the client's own list of animals. */
public class HomeActivity extends AppCompatActivity {

    String username;
    Client client;
    Animal animal;
    AutoCompleteTextView ageTextView, breedTextview, sexTextView;
    ArrayAdapter<String> adapterAges, adapterBreeds, adapterSexes;
    String selectedType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        client = ClientManager.getClient();

        if(client.isGuest()) {
            /* Hide relavent buttons only for user. */
            ((TextView)findViewById(R.id.usernameID)).setText("Guest");
            ((Button)findViewById(R.id.myAnimals)).setVisibility(View.GONE);
            ((Button)findViewById(R.id.setAnimalForAdoptionID)).setVisibility(View.GONE);
        } else {
            username = getIntent().getStringExtra("username");
            if(client != null && client.isConnected()) {
                ((TextView)findViewById(R.id.usernameID)).setText(getIntent().getStringExtra("username"));
            }
        }

        fetchRandomAnimal();
    }

    /* Puts the animal's details on the screen. */
    public void fetchRandomAnimal() {
        animal = client.getRandomAnimal();

        if (animal != null) {
            setAnimalProfile(animal);
        } else {
            Toast.makeText(this, "Failed to fetch animal.", Toast.LENGTH_SHORT).show();
        }
    }

    /* Sets the animal's profile. */
    public void setAnimalProfile(Animal animal){
        byte[] decodedString = Base64.decode(animal.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ((ImageView)findViewById(R.id.animalimage)).setImageBitmap(decodedByte);

        ((TextView)findViewById(R.id.animalname)).setText(animal.getName());
        ((TextView)findViewById(R.id.animalbreed)).setText(animal.getBreed());
        ((TextView)findViewById(R.id.animalage)).setText(animal.getAge());
        ((TextView)findViewById(R.id.animalsex)).setText(animal.getSex());
    }

    /* Sends the client to the adoption screen. */
    public void setAnimalForAdoption(View view){
        Intent intent = new Intent(HomeActivity.this, SetAdoptionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /* Shows the animal and owner's info. */
    public void showAnimalDescriptionAndOwnerInfoClicked(View view){
        Intent intent = new Intent(HomeActivity.this, AnimalAndOwnerInfoActivity.class);
        intent.putExtra("animal", animal);
        startActivity(intent);
    }

    /* Shows the list of the user's animals. */
    public void myAnimalsClicked(View view){

    }

    /* Fetches animal corresponding to given attributes. */
    public void fetchAnimalByAttributes(String age, String breed, String sex, String type) {
        String selectedAge = ageTextView.getText().toString();
        String selectedBreed = breedTextview.getText().toString();
        String selectedSex = sexTextView.getText().toString();

        if (selectedAge.isEmpty()) selectedAge = null;
        if (selectedBreed.isEmpty()) selectedBreed = null;
        if (selectedSex.isEmpty()) selectedSex = null;

        animal = client.getAnimalByAttributes(selectedAge, selectedBreed, selectedSex, selectedType);

        if(animal != null){
            setAnimalProfile(animal);
        } else {
            Toast.makeText(this, "No animals found with the selected attributes.", Toast.LENGTH_SHORT).show();
        }
    }

    /* Fetches the next animal - either random or corresponding to selected attributes. */
    public void nextAnimalClicked(View view){
        if(selectedType == null)
            fetchRandomAnimal();
        else {
            String selectedAge = ageTextView.getText().toString();
            String selectedBreed = breedTextview.getText().toString();
            String selectedSex = sexTextView.getText().toString();

            fetchAnimalByAttributes(selectedAge, selectedBreed, selectedSex, selectedType);
        }
    }

    /* Sets dog possible attributes. */
    public void dogButtonClicked(View view){
        String[] viableAges = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] viableBreeds = { "Belgian Malinois", "German Shepherd", "Golden Retriever", "Labrador Retriever"};
        String[] viableSexes = {"M", "F"};
        selectedType = "Dog";

        resetCategories();
        setCategories(viableAges, viableBreeds, viableSexes);
    }

    /* Sets cat possible attributes. */
    public void catButtonClicked(View view){
        String[] viableAges = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] viableBreeds = {"Abyssinian", "Bengal", "Maine Coon", "Persian", "Ragdoll", "Sphynx"};
        String[] viableSexes = {"M", "F"};
        selectedType = "Cat";

        resetCategories();
        setCategories(viableAges, viableBreeds, viableSexes);
    }

    /* Sets bird possible attributes. */
    public void birdButtonClicked(View view){
        String[] viableAges = {"0", "1", "2", "3", "4", "5"};
        String[] viableBreeds = {"Budgerigar", "Canary", "Cocktail", "Lovebird", "Macaw", "Parrotlet"};
        String[] viableSexes = {"M", "F"};
        selectedType = "Bird";

        resetCategories();
        setCategories(viableAges, viableBreeds, viableSexes);
    }

    /* Sets rabbit possible attributes. */
    public void rabbitButtonClicked(View view){
        String[] viableAges = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
        String[] viableBreeds = {"English Angora", "Flemish Giant", "Netherland Dwarf", "Holland Lop", "Lionhead", "Mini Rex"};
        String[] viableSexes = {"M", "F"};
        selectedType = "Rabbit";

        resetCategories();
        setCategories(viableAges, viableBreeds, viableSexes);
    }

    /* Rests the attributes. */
    public void resetCategories(){
        if (ageTextView != null) ageTextView.setText("");
        if (breedTextview != null) breedTextview.setText("");
        if (sexTextView != null) sexTextView.setText("");

        if (adapterAges != null) {
            assert ageTextView != null;
            ageTextView.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, new String[]{}));
        }
        if (adapterBreeds != null) {
            assert breedTextview != null;
            breedTextview.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, new String[]{}));
        }
        if (adapterSexes != null) {
            assert sexTextView != null;
            sexTextView.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, new String[]{}));
        }
    }

    /* Sets the attributes corresponding to selected type. */
    public void setCategories(String[] viableAges, String[] viableBreeds, String[] viableSexes){
        setAgeAdapter(viableAges);
        setBreedAdapter(viableBreeds);
        setSexAdapter(viableSexes);
    }

    /* Sets age dropdown menu. */
    public void setAgeAdapter(String[] viableAges){
        ageTextView = findViewById(R.id.ageTextView);
        adapterAges = new ArrayAdapter<String>(this, R.layout.list_item, viableAges);
        ageTextView.setAdapter(adapterAges);
        ageTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String age = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(HomeActivity.this, "age: " + age, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Sets breed dropdown menu. */
    public void setBreedAdapter(String[] viableBreeds){
        breedTextview = findViewById(R.id.breedTextView);
        adapterBreeds = new ArrayAdapter<String>(this, R.layout.list_item, viableBreeds);
        breedTextview.setAdapter(adapterBreeds);
        breedTextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String breed = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(HomeActivity.this, "breed: " + breed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Sets sex dropdown menu. */
    public void setSexAdapter(String[] viableSexes){
        sexTextView = findViewById(R.id.sexTextView);
        adapterSexes = new ArrayAdapter<String>(this, R.layout.list_item, viableSexes);
        sexTextView.setAdapter(adapterSexes);
        sexTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sex = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(HomeActivity.this, "sex: " + sex, Toast.LENGTH_SHORT).show();
            }
        });
    }

}