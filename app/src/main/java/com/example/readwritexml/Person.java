package com.example.readwritexml;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Person {
    String first_name;
    String last_name;
    Person(){}
    public Person(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    @Override
    public String toString() {
        return "" +
                "first_name=  '" + first_name + '\'' +
                "  , last_name=  '" + last_name + '\'';
    }
}
