package org.example.test_1_server;


public class RegisterLoginRequest {
    public String username;
    public String password;

    public RegisterLoginRequest() {}

    public RegisterLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

