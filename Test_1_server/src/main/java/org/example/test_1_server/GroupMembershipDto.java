package org.example.test_1_server;

public class GroupMembershipDto {
    public int groupId;
    public int userId;

    public GroupMembershipDto() {}

    public GroupMembershipDto(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
    }
}
