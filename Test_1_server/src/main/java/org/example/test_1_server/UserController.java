package org.example.test_1_server;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserDto dto) {
        System.out.println("HIT /api/users/register, username=" + dto.username);
        boolean ok = service.register(dto);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto dto) {
        UserDto user = service.login(dto.username, dto.password);
        return user == null ? ResponseEntity.status(401).body("Bad credentials") : ResponseEntity.ok(user);
    }

    @GetMapping("/find")
    public ResponseEntity<UserDto> findUser(@RequestParam String value) {
        UserDto user = service.findUser(value);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping("/save")
    public ResponseEntity<Void> saveUser(@RequestBody UserDto dto) {
        boolean ok = service.saveUser(dto);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }

}

