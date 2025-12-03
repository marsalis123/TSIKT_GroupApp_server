package org.example.test_1_server;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService service;
    private final NotificationService notificationService;

    public JobController(JobService service, NotificationService notificationService) {
        this.service = service;
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<JobDto> getUserJobs(@RequestParam int userId) {
        return service.getUserJobs(userId);
    }

    @PostMapping
    public ResponseEntity<Void> addJob(@RequestBody JobDto dto) {
        boolean ok = service.addJob(dto);
        if (ok) {
            notificationService.sendToGroup(
                    dto.groupId,
                    new NotificationDto(
                            "JOB",
                            "Nová úloha: " + dto.title,
                            dto.groupId
                    )
            );
        }
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateJob(@PathVariable int id, @RequestBody JobDto dto) {
        boolean ok = service.updateJob(id, dto);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }

    @PostMapping("/logs")
    public ResponseEntity<Void> addLog(@RequestBody JobLogDto dto) {
        boolean ok = service.addJobLog(dto);
        if (ok) {
            Integer groupId = service.getJobGroupId(dto.jobId);
            if (groupId != null) {
                notificationService.sendToGroup(
                        groupId,
                        new NotificationDto(
                                "JOB_LOG",
                                "Nový zápis k úlohe #" + dto.jobId,
                                groupId
                        )
                );
            }
        }
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }



    @GetMapping("/{id}/logs")
    public List<JobLogDto> getLogs(@PathVariable int id) {
        return service.getJobLogs(id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable int id,
                                             @RequestParam String status) {
        boolean ok = service.updateJobStatus(id, status);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(400).build();
    }

}
