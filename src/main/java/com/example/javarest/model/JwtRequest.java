package com.example.javarest.model;

public class JwtRequest {

    private String login;
    private String password;

    public JwtRequest() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}