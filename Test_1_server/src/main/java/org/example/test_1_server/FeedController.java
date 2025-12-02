package org.example.test_1_server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@CrossOrigin(origins = "*")
public class FeedController {

    private final FeedService service;

    public FeedController(FeedService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> addMessage(@RequestBody FeedMessageDto dto) {
        boolean ok = service.addFeedMessage(dto);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }

    @GetMapping
    public List<FeedMessageDto> getMessages(@RequestParam int groupId) {
        return service.getFeedMessages(groupId);
    }
}
