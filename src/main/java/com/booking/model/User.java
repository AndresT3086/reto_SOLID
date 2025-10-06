package com.booking.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String department;
    private UserType type;

    public enum UserType {
        REGULAR,
        MANAGER,
        ADMIN
    }

    public User(String id, String name, String email, String department, UserType type) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public UserType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', email='%s', type=%s}",
                id, name, email, type);
    }
}