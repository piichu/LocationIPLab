package com.example.locationip.controller;

import com.example.locationip.model.IP;
import com.example.locationip.model.Location;
import com.example.locationip.model.Tag;
import com.example.locationip.service.IPService;
import com.example.locationip.service.LocationService;
import com.example.locationip.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    public final LocationService locationService;
    public final IPService ipService;
    public final TagService tagService;

    public LocationController(LocationService locationService, IPService ipService, TagService tagService) {
        this.locationService = locationService;
        this.ipService = ipService;
        this.tagService = tagService;
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
    public ResponseEntity<Location> getLocationByIP(@RequestParam String address) {
        Location newLocation = locationService.getLocationByIP(ipService.getIPByAddress(address));
        if (newLocation == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(newLocation);
    }

    @PostMapping("/linked/{ipId}/{tagId}")
    public ResponseEntity<String> createLinkedLocation(@RequestBody Location location, @PathVariable Long ipId, @PathVariable Long tagId) {
        try {
            Tag tag = tagService.getTagById(tagId);
            IP ip = ipService.getIPById(ipId);
            if (tag != null && ip != null) {
                location.getIps().add(ip);
                ip.setLocation(location);
                location.getTags().add(tag);
                tag.getLocations().add(location);
                locationService.saveLocation(location);
                return ResponseEntity.status(HttpStatus.CREATED).body("Location created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body("Bad IP or tag.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add location");
        }
    }

    @PostMapping()
    public ResponseEntity<String> createLocation(@RequestBody Location location) {
        try {
            locationService.saveLocation(location);
            return ResponseEntity.status(HttpStatus.CREATED).body("Location created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add location");
        }
    }

    @PutMapping("/linkIP/{locationId}/{ipId}")
    public ResponseEntity<String> addIPToLocation(@PathVariable Long locationId, @PathVariable Long ipId) {
        try {
            Location location = locationService.getLocationById(locationId);
            IP ip = ipService.getIPById(ipId);
            if (location == null || ip == null) {
                return ResponseEntity.badRequest().body("Invalid location or IP ID");
            }
            ip.setLocation(location);
            location.getIps().add(ip);
            locationService.saveLocation(location);
            return ResponseEntity.ok("IP successfully added to location.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add ip to location");
        }
    }

    @PutMapping("/linkTag/{locationId}/{tagId}")
    public ResponseEntity<String> addTagToLocation(@PathVariable Long locationId, @PathVariable Long tagId) {
        try {
            Location location = locationService.getLocationById(locationId);
            Tag tag = tagService.getTagById(tagId);

            if (location == null || tag == null) {
                return ResponseEntity.notFound().build();
            }
            if(location.getTags().contains(tag)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Location already has this tag");
            }
            location.getTags().add(tag);
            tag.getLocations().add(location);
            locationService.saveLocation(location);
            return ResponseEntity.ok("Tag added to location successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add tag to location");
        }
    }

    @PutMapping("/unlinkIP/{locationId}/{ipId}")
    public ResponseEntity<String> unlinkIP(@PathVariable Long locationId, @PathVariable Long ipId) {
        try {
            Location location = locationService.getLocationById(locationId);
            IP ip = ipService.getIPById(ipId);
            if (location == null || ip == null) {
                return ResponseEntity.badRequest().body("Invalid location or IP ID");
            }
            ip.setLocation(null);
            location.getIps().remove(ip);
            locationService.saveLocation(location);
            return ResponseEntity.ok("IP successfully unlinked to location.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unlink ip to location");
        }

    }

    @PutMapping("/unlinkTag/{locationId}/{tagId}")
    public ResponseEntity<String> unlinkTag(@PathVariable Long locationId, @PathVariable Long tagId) {
        try {
            Location location = locationService.getLocationById(locationId);
            Tag tag = tagService.getTagById(tagId);
            if (location == null || tag == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Not found tag or location");
            }
            location.getTags().remove(tag);
            tag.getLocations().remove(location);
            locationService.saveLocation(location);
            return ResponseEntity.ok("Tag unlinked to location successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unlink tag to location");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateLocation(@PathVariable Long id, @RequestBody Location location) {
        if (!locationService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Requested location don't exist");
        }
        Location oldLocation = locationService.getLocationById(id);
        location.setId(id);
        location.setIps(oldLocation.getIps());
        location.setTags(oldLocation.getTags());
        locationService.saveLocation(location);
        return ResponseEntity.ok("Location has been changed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        if (!locationService.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Requested locations don't exist");
        }
        locationService.deleteLocation(id);
        return ResponseEntity.ok("Location and linked IP have been deleted");
    }
}