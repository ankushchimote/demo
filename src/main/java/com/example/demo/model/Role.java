package com.example.demo.model;

public enum Role {
    USER,
    ADMIN
}

//Why an enum?
//Instead of allowing arbitrary strings like:
//        "admin"
//        "Admin"
//        "administrator"
//        "superuser"
//
//we restrict the role to known values:
//Role.USER
//Role.ADMIN