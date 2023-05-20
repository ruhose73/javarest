package com.example.javarest.exceprion;

public class WrongDataException extends Exception{
    public WrongDataException(String message) {
        super(message);
    }
}
