package dev.rangel.eduflow.notificationservice.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class AnnouncementController {

    @Value("${platform.announcement:No announcement configured}")
    private String announcement;

    @GetMapping("/system/announcement")
    public String getAnnouncement() {
        return announcement;
    }
}