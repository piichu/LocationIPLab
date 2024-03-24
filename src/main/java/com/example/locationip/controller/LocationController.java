package com.example.locationip.controller;

import com.example.locationip.model.Location;
import com.example.locationip.service.LocationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
public class LocationController {
    public final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationService.getAllLocations();
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @GetMapping("/ip")
    public Location getLocationByIP(@RequestParam String address) {
        return locationService.getLocationByIP(address);
    }

    @GetMapping("/tag")
    public List<Location> getLocationByTag(@RequestParam String name) {
        return locationService.getLocationByTag(name);
    }

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Long createLocation(@RequestParam String country,
                               @RequestParam String city,
                               @RequestBody(required = false) List<Long> tags) {
        return locationService.createLocation(country, city, tags);
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateLocation(@PathVariable Long id,
                               @RequestParam(required = false) String country,
                               @RequestParam(required = false) String city,
                               @RequestBody(required = false) Map<String, List<Long>> ids) {
        List<Long> ips = ids.get("ips");
        List<Long> tags = ids.get("tags");
        locationService.updateLocation(id, country, city, ips, tags);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocationById(id);
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/addIp")
    public void addIPToLocation(@RequestParam Long locationId, @RequestParam Long ipId) {
        locationService.addIPToLocation(locationId, ipId);
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/addTag")
    public void addTagToLocation(@RequestParam Long locationId, @RequestParam Long tagId) {
        locationService.addTagToLocation(locationId, tagId);
    }
}