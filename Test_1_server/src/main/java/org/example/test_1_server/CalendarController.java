package org.example.test_1_server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "*")
public class CalendarController {

    private final CalendarService service;
    private final NotificationService notificationService;

    public CalendarController(CalendarService service, NotificationService notificationService) {
        this.service = service;
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Void> addEvent(@RequestBody CalendarEventDto dto) {
        boolean ok = service.addEvent(dto);
        if (ok) {
            notificationService.sendToGroup(
                    dto.groupId,
                    new NotificationDto(
                            "CALENDAR",
                            "Nový termín: " + dto.title + " (" + dto.date + ")",
                            dto.groupId
                    )
            );
        }
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }


    // events pre 1 skupinu a mesiac "YYYY-MM"
    @GetMapping("/group")
    public List<CalendarEventDto> getGroupEvents(@RequestParam int groupId,
                                                 @RequestParam String yearMonth) {
        return service.getEventsForGroupMonth(groupId, yearMonth);
    }

    // events pre viac skupín usera, param: ?yearMonth=YYYY-MM&groupIds=1,2,3
    @GetMapping("/user")
    public List<CalendarEventDto> getUserGroupsEvents(@RequestParam String yearMonth,
                                                      @RequestParam String groupIds) {
        String[] parts = groupIds.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String p : parts) {
            if (!p.isBlank()) ids.add(Integer.parseInt(p.trim()));
        }
        YearMonth ym = YearMonth.parse(yearMonth);
        return service.getEventsForUsersGroups(ids, ym);
    }
}
