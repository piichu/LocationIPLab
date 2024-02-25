package com.example.locationip.controller;

import com.example.locationip.service.LocationService;
import com.example.locationip.Location;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@RestController
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/location/{ipAddress}")
    public ResponseEntity<Location> getLocationByIPAddress(@PathVariable String ipAddress) {
        Location location = locationService.getLocationByIPAddress(ipAddress);
        return ResponseEntity.ok(location);
    }
}