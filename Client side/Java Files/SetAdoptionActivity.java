package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;

/* Sets an animal for adoption.  */
public class SetAdoptionActivity extends AppCompatActivity {

    String username, animalName, currentType, age, breed, sex, description;
    Client client;
    AutoCompleteTextView typeTextView, ageTextView, breedTextview, sexTextView;
    ArrayAdapter<String> adapterTypes, adapterAges, adapterBreeds, adapterSexes;
    ActivityResultLauncher<Intent> resultLauncher;
    String[] types = {"Dog", "Cat", "Bird", "Rabbit"};
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_adoption);

        client = ClientManager.getClient();
        username = getIntent().getStringExtra("username");
        if(client != null && client.isConnected()){
            ((TextView)findViewById(R.id.usernameID)).setText(username);
        }

        setTypeAdapter();
        registerResult();
    }

    /* Sets type dropdown menu. */
    public void setTypeAdapter(){
        typeTextView = findViewById(R.id.typeTextView);
        adapterTypes = new ArrayAdapter<String>(this, R.layout.list_item, types);
        typeTextView.setAdapter(adapterTypes);
        typeTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentType = adapterView.getItemAtPosition(i).toString();
                setCategoriesCorrespondingToType(currentType);
            }
        });
    }

    /* Sets attributes for selected type. */
    public void setCategoriesCorrespondingToType(String type){

        if(type.equals("Dog")){
            String[] viableAges = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            String[] viableBreeds = { "Belgian Malinois", "German Shepherd", "Golden Retriever", "Labrador Retriever"};
            String[] viableSexes = {"M", "F"};
            resetCategories();
            setCategories(viableAges, viableBreeds, viableSexes);
        } else if(type.equals("Cat")){
            String[] viableAges = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
            String[] viableBreeds = {"Abyssinian", "Bengal", "Maine Coon", "Persian", "Ragdoll", "Sphynx"};
            String[] viableSexes = {"M", "F"};
            resetCategories();
            setCategories(viableAges, viableBreeds, viableSexes);
        } else if(type.equals("Bird")){
            String[] viableAges = {"0", "1", "2", "3", "4", "5"};
            String[] viableBreeds = {"Budgerigar", "Canary", "Cocktail", "Lovebird", "Macaw", "Parrotlet"};
            String[] viableSexes = {"M", "F"};
            resetCategories();
            setCategories(viableAges, viableBreeds, viableSexes);
        } else if(type.equals("Rabbit")){
            String[] viableAges = {"0", "1", "2", "3", "4", "5", "6", "7", "8"};
            String[] viableBreeds = {"English Angora", "Flemish Giant", "Netherland Dwarf", "Holland Lop", "Lionhead", "Mini Rex"};
            String[] viableSexes = {"M", "F"};
            resetCategories();
            setCategories(viableAges, viableBreeds, viableSexes);
        }
    }

    /* Resets attributes. */
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

    /* Sets attributes with given string arrays. */
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
                age = adapterView.getItemAtPosition(i).toString();
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
                breed = adapterView.getItemAtPosition(i).toString();
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
                sex = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    /* Sets image that's given by the user to a bitmap and presents it on the screen. */
    private void registerResult(){
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try{
                    Uri imageUri = result.getData().getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ((ImageView)findViewById(R.id.UploadAnimalPictureID)).setImageURI(imageUri);
                } catch (Exception e){
                    Toast.makeText(SetAdoptionActivity.this, "No image selected.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /* Sends the user to the image selection screen. */
    public void uploadAnimalPictureButtonClicked(View view){
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        resultLauncher.launch(intent);
    }

    /* Sends a 'set-adoption' request to the server with the given animal info. */
    public void setAnimalForAdoption(View view){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        animalName = ((EditText)findViewById(R.id.animalNameID)).getText().toString();
        description = ((EditText)findViewById(R.id.descriptionID)).getText().toString();

        if(bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            final String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            client.addAnimalToDatabase(username, animalName, currentType, age, breed, sex, base64Image, description);
        } else {
            Toast.makeText(SetAdoptionActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

}