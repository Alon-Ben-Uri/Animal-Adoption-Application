package com.example.myapplication;

import java.io.Serializable;

/* Animal class describes an animal as well as the users name and email. */
public class Animal implements Serializable {

    String name, type, breed, age, sex, image, description, username, email;

    public Animal(String name, String type, String breed, String age, String sex, String image, String description, String username, String email) {
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.sex = sex;
        this.image = image;
        this.description = description;
        this.username = username;
        this.email = email;
    }

    public String getName() { return name; }
    public String getBreed() { return breed; }
    public String getSex() { return sex; }
    public String getAge() { return age; }
    public String getImage() { return image; }
    public String getDescription() { return description; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return name + "|" + type + "|" + breed + "|" + age + "|" + sex + "|" + image + "|" + description + "|" + username + "|" + email;
    }
}
