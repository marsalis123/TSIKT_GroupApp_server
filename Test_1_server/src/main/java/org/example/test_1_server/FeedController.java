package org.example.test_1_server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@CrossOrigin(origins = "*")
public class FeedController {

    private final FeedService service;
    private final NotificationService notificationService;

    public FeedController(FeedService service, NotificationService notificationService) {
        this.service = service;
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> addMessage(@RequestBody FeedMessageDto dto) {
        boolean ok = service.addFeedMessage(dto);
        if (ok) {
            notificationService.sendToGroup(
                    dto.groupId,
                    new NotificationDto("FEED",
                            "Nová správa vo feede skupiny " + dto.groupId,
                            dto.groupId)
            );
        }
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }


    @GetMapping
    public List<FeedMessageDto> getMessages(@RequestParam int groupId) {
        return service.getFeedMessages(groupId);
    }


}
