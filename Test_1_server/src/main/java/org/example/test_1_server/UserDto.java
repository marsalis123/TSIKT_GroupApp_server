package org.example.test_1_server;


public class UserDto {
    public int id;
    public String username;
    public String name;
    public String email;
    public int age;
    public String photoPath;

    public UserDto() {}

    public UserDto(int id, String username, String name, String email, int age, String photoPath) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.age = age;
        this.photoPath = photoPath;
    }
}
