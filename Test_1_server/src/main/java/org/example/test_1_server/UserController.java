package org.example.test_1_server;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterLoginRequest req) {
        boolean ok = service.register(req.username, req.password);
        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.status(400).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody RegisterLoginRequest req) {
        UserDto user = service.login(req.username, req.password);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(user);
    }
}

