package com.example.locationip.controller;

import com.example.locationip.service.LocationService;
import com.example.locationip.model.Location;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/location")
    public ResponseEntity<Location> getLocationByIPAddress(@RequestParam("ipAddress") String ipAddress) {
        Location location = locationService.getLocationByIPAddress(ipAddress);
        return ResponseEntity.ok(location);
    }
}