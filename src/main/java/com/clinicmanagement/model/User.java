package com.clinicmanagement.model;

public class User {
    public int id;
    public String lastName;
    public String firstName;
    public String username;
    public String role;

    public User(int id, String lastName, String firstName, String username, String role) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.username = username;
        this.role = role;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
