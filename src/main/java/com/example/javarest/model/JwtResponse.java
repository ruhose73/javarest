package com.example.javarest.model;

public class JwtResponse {

    private final String type = "Bearer";
    private String accessToken;

    public JwtResponse() {}

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getType() {
        return type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}