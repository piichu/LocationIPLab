package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.IP;
import com.example.locationip.model.Location;
import com.example.locationip.model.Tag;
import com.example.locationip.repository.IPRepository;
import com.example.locationip.repository.LocationRepository;
import com.example.locationip.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final IPRepository ipRepository;
    private final TagRepository tagRepository;
    private final Cache cache;
    private static final String CACHE_KEY = "location-";

    public LocationService(LocationRepository locationrepository, IPRepository ipRepository, TagRepository tagRepository, Cache cache) {
        this.locationRepository = locationrepository;
        this.ipRepository = ipRepository;
        this.tagRepository = tagRepository;
        this.cache = cache;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        if (cache.containsKey(CACHE_KEY + id)) {
            return (Location) cache.getFromCache(CACHE_KEY + id);
        }
        Location location = locationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        cache.putToCache(CACHE_KEY + id, location);
        return location;
    }

    public Location getLocationByIP(String address) {
        return locationRepository.findLocationByIP(address);
    }

    public List<Location> getLocationByTag(String name) {
        return locationRepository.findLocationByTag(name);
    }

    public Long createLocation(String country, String city, List<Long> tagsIds) {
        Location location = new Location();
        location.setCountry(country);
        location.setCity(city);
        if (tagsIds != null) {
            List<Tag> tagList = new ArrayList<>();
            for (Long tagsId : tagsIds) {
                tagList.add(tagRepository.getTagById(tagsId));
            }
            location.setTags(tagList);
        }
        locationRepository.save(location);
        cache.putToCache(CACHE_KEY + location.getId(), location);
        return location.getId();
    }

    public void updateLocation(Long id, String country, String city, List<Long> ipsIds, List<Long> tagsIds) {
        Location location;
        if (cache.containsKey(CACHE_KEY + id)) {
            location = (Location) cache.getFromCache(CACHE_KEY + id);
        } else {
            location = getLocationById(id);
        }
        if (country != null) {
            location.setCountry(country);
        }
        if (city != null) {
            location.setCity(city);
        }
        if (!ipsIds.isEmpty()) {
            List<IP> ipList = new ArrayList<>();
            for (Long ipsId : ipsIds) {
                ipList.add(ipRepository.getIPById(ipsId));
            }
            location.setIps(ipList);
        }
        if (!tagsIds.isEmpty()) {
            List<Tag> tagList = new ArrayList<>();
            for (Long tagsId : tagsIds) {
                tagList.add(tagRepository.getTagById(tagsId));
            }
            location.setTags(tagList);
        }
        locationRepository.save(location);
        cache.putToCache(CACHE_KEY + id, location);
    }

    public void deleteLocationById(Long id) {
        locationRepository.deleteById(id);
        cache.removeFromCache(CACHE_KEY + id);
    }

    public void addIPToLocation(Long locationId, Long ipId) {
        IP ip = ipRepository.getIPById(ipId);
        Location location = getLocationById(locationId);
        if (location != null && ip != null) {
            location.getIps().add(ip);
            locationRepository.save(location);
            cache.putToCache(CACHE_KEY + locationId, location);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void addTagToLocation(Long locationId, Long tagId) {
        Tag tag = tagRepository.getTagById(tagId);
        Location location = getLocationById(locationId);
        if (location != null && tag != null) {
            tag.getLocations().add(location);
            locationRepository.save(location);
            cache.putToCache(CACHE_KEY + locationId, location);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}