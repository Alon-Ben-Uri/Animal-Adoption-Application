package com.example.myapplication;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/* Client communication with the server. */
public class Client extends Thread {
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private boolean clientExistenceStatus = false;
    private boolean isGuest = false;

    /* Connects the client to the database. */
    @Override
    public void run() {
        try {
            socket = new Socket("10.0.2.2", 3000);  // Replace with your server's IP and port
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Sends a sign up request to the server. Parameter string contains all user info. */
    public void sendSignupRequest(String signupRequest){
        new Thread(() -> {
            try {
                if(outputStream != null) {
                    // send login request to server (contains input username & password.
                    outputStream.writeUTF(signupRequest);
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /* Attempts login to the server. Parameter string contains potential user info. */
    public void loginAttempt(String loginRequest) {
        new Thread(() -> {
            try {
                if(outputStream != null) {
                    // send login request to server (contains input username & password.
                    outputStream.writeUTF(loginRequest);
                    outputStream.flush();

                    // updates clientExistenceStatus accordingly.
                    if((inputStream.readUTF()).equals("login successful"))
                        this.clientExistenceStatus = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /* Adds animal to the database. */
    public void addAnimalToDatabase(String username, String animalName, String animalType, String animalAge, String animalBreed, String animalSex, String base64Image, String description){
        new Thread(() -> {
            try{
                if(outputStream != null){
                    outputStream.writeUTF("addAnimalToDatabase|" + username + "|" + animalName + "|" + animalType + "|" + animalAge + "|" + animalBreed + "|" + animalSex + "|" + base64Image + "|" + description);
                    outputStream.flush();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }

    /* Returns a random animal from the database. */
    public Animal getRandomAnimal() {
        final AtomicReference<Animal> randomAnimal = new AtomicReference<>(null);

        Thread thread = new Thread(() -> {
            try {
                if (outputStream != null && inputStream != null) {
                    outputStream.writeUTF("getRandomAnimal");
                    outputStream.flush();

                    String response = inputStream.readUTF();
                    String[] animalData = response.split("\\|");

                    randomAnimal.set(new Animal(
                            animalData[0],  // name
                            animalData[1],  // type
                            animalData[2],  // breed
                            animalData[3],  // age
                            animalData[4],  // sex
                            animalData[5],  // image
                            animalData[6],  // description
                            animalData[7],  // username
                            animalData[8]   // email
                    ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        try {
            thread.join();  // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return randomAnimal.get();
    }

    /* Returns an animal that answers the requested attributes. */
    public Animal getAnimalByAttributes(String age, String breed, String sex, String type) {
        final AtomicReference<Animal> animal = new AtomicReference<>(null);
        Thread thread = new Thread(() -> {
            try {
                outputStream.writeUTF("getAnimalByAttributes|" + age + "|" + breed + "|" + sex + "|" + type);
                outputStream.flush();

                String response = inputStream.readUTF();
                if (!response.equals("null")) {
                    animal.set(parseAnimalFromResponse(response));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return animal.get();
    }

    /* Returns a new animal corresponding to the string. */
    public Animal parseAnimalFromResponse(String response) {
        String[] parts = response.split("\\|");
        if (parts.length >= 7) {
            String name = parts[0];
            String type = parts[1];
            String breed = parts[2];
            String age = parts[3];
            String sex = parts[4];
            String image = parts[5];
            String description = parts[6];

            return new Animal(name, type, breed, age, sex, image, description, "", "");
        }
        return null;
    }

    /* Gets client's existence status.*/
    public boolean getClientExistenceStatus(){
        return clientExistenceStatus;
    }

    /* Sets the client to be a guest. */
    public void setGuest(){
        isGuest = true;
    }

    /* Returns true if the client is a guest. */
    public boolean isGuest() { return isGuest; }

    /* Checks if the client is connected to the server. */
    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    /* Disconnects client from the server. */
    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}