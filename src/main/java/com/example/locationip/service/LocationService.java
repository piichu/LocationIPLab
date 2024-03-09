package com.example.locationip.service;

import com.example.locationip.model.IP;
import com.example.locationip.model.Location;
import com.example.locationip.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationrepository) {
        this.locationRepository = locationrepository;
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return locationRepository.existsById(id);
    }

    public Location getLocationByIP(IP ip) {
        List<Location> locationList = locationRepository.findAll();
        for (Location location : locationList) {
            if (location.getIps().contains(ip)) {
                return location;
            }
        }
        return null;
    }
}