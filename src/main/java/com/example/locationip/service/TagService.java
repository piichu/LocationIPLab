package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.Location;
import com.example.locationip.model.Tag;
import com.example.locationip.repository.LocationRepository;
import com.example.locationip.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final LocationRepository locationRepository;
    private final Cache cache;
    private static final String CACHE_KEY = "tag-";

    public TagService(TagRepository tagRepository, LocationRepository locationRepository, Cache cache) {
        this.tagRepository = tagRepository;
        this.locationRepository = locationRepository;
        this.cache = cache;
    }

    public Tag getTagById(Long id) {
        if(cache.containsKey(CACHE_KEY+id)){
            return (Tag) cache.getFromCache(CACHE_KEY+id);
        }
        Tag tag= tagRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        cache.putToCache(CACHE_KEY+id, tag);
        return tag;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Long createTag(String name, List<Long> locationsIds) {
        Tag tag = new Tag();
        tag.setName(name);
        if (locationsIds != null) {
            for (Long locationId : locationsIds) {
                tag.getLocations().add(locationRepository.getLocationById(locationId));
            }
        }
        tagRepository.save(tag);
        cache.putToCache(CACHE_KEY+tag.getId(), tag);
        return tag.getId();
    }

    public void updateTag(Long id, String name, List<Long> locationsIds) {
        Tag tag;
        if(cache.containsKey(CACHE_KEY+id)){
            tag=(Tag) cache.getFromCache(CACHE_KEY+id);
        }else {
            tag=tagRepository.getTagById(id);
        }
        if (name != null) tag.setName(name);
        if (locationsIds != null) {
            List<Location> locationList = new ArrayList<>();
            for (Long locationId : locationsIds) {
                locationList.add(locationRepository.getLocationById(locationId));
            }
            tag.setLocations(locationList);
        }
        tagRepository.save(tag);
        cache.putToCache(CACHE_KEY+id, tag);
    }

    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
        cache.removeFromCache(CACHE_KEY+id);
    }

    public void addLocationToTag(Long tagId, Long locationId) {
        Tag tag = tagRepository.getTagById(tagId);
        Location location = locationRepository.getLocationById(locationId);
        if (location != null && tag != null) {
            tag.getLocations().add(location);
            tagRepository.save(tag);
            cache.putToCache(CACHE_KEY+tagId, tag);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
