package org.example.test_1_server;


public class GroupDto {
    public int id;
    public String name;
    public String owner;

    public GroupDto() {}

    public GroupDto(int id, String name, String owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
    }
}
