package org.example.test_1_server;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @GetMapping
    public List<GroupDto> getUserGroups(@RequestParam String username) {
        return service.getUserGroups(username);
    }

    @PostMapping
    public ResponseEntity<Void> createGroup(@RequestBody GroupDto dto) {
        boolean ok = service.addGroup(dto.name, dto.owner);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable int id) {
        boolean ok = service.deleteGroup(id);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }
    @PostMapping("/members")
    public ResponseEntity<Void> addMember(@RequestBody GroupMembershipDto dto) {
        boolean ok = service.addUserToGroup(dto.groupId, dto.userId);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }

    @DeleteMapping("/members")
    public ResponseEntity<Void> removeMember(@RequestBody GroupMembershipDto dto) {
        boolean ok = service.removeUserFromGroup(dto.groupId, dto.userId);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }
    @GetMapping("/{id}/members")
    public List<UserDto> getMembers(@PathVariable int id) {
        return service.getGroupMembers(id);
    }



}
