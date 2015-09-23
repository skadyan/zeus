package com.cumulativeminds.zeus.api.controller;

import java.time.LocalDateTime;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@Path("ping")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PingController {

    public PingController() {
    }

    @GET
    public Health health() {
        return Health.status(Status.UP)
                .withDetail("serverTime", LocalDateTime.now())
                .build();
    }

    @GET
    @Path("/error")
    public Health healthError() {
        throw new RuntimeException("Something Weired!");
    }

}
