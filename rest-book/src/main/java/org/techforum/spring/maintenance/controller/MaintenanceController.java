package org.techforum.spring.maintenance.controller;

import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.techforum.spring.maintenance.dto.MaintenanceDTO;

import javax.validation.constraints.NotNull;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Manages maintenance mode flag by using Spring boot standard functionalities
 * <p>
 * These classes interact with Actuator and its Readiness Probe.
 *
 * @see ApplicationEventPublisher
 * @see org.springframework.boot.availability.ApplicationAvailability
 * @see ReadinessState
 */
@RestController()
@RequestMapping(value = MaintenanceController.API_MAINTENANCE_URI, produces = APPLICATION_JSON_VALUE)
public class MaintenanceController {


    public static final String API_MAINTENANCE_URI = "/api/maintenance";
    private ApplicationEventPublisher eventPublisher;

    private ApplicationAvailability availability;

    public MaintenanceController(ApplicationEventPublisher eventPublisher, ApplicationAvailability applicationAvailability) {
        this.eventPublisher = eventPublisher;
        this.availability = applicationAvailability;
    }

    @GetMapping
    public ResponseEntity<MaintenanceDTO> retreiveInMaintenance() {
        var lastChangeEvent = availability.getLastChangeEvent(ReadinessState.class);
        return ResponseEntity.ok(new MaintenanceDTO(lastChangeEvent.getState().equals(ReadinessState.REFUSING_TRAFFIC), new Date(lastChangeEvent.getTimestamp())));
    }

    @PutMapping
    public ResponseEntity<Void> initInMaintenance(@NotNull @RequestBody String inMaintenance) {
        AvailabilityChangeEvent.publish(eventPublisher, this, Boolean.valueOf(inMaintenance) ? ReadinessState.REFUSING_TRAFFIC : ReadinessState.ACCEPTING_TRAFFIC);
        return ResponseEntity.noContent().build();
    }
}
