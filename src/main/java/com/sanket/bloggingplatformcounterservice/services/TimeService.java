package com.sanket.bloggingplatformcounterservice.services;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeService {

    public Long getEpochTime() {
        return Instant.now().toEpochMilli();
    }
}
