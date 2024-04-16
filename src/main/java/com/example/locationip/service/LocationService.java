package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.Ip;
import com.example.locationip.model.Location;
import com.example.locationip.model.Tag;
import com.example.locationip.repository.IpRepository;
import com.example.locationip.repository.LocationDto;
import com.example.locationip.repository.LocationRepository;
import com.example.locationip.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/** The type Location service. */
@Service
public class LocationService {
  private static final String CACHE_KEY = "location-";
  private final LocationRepository locationRepository;
  private final IpRepository ipRepository;
  private final TagRepository tagRepository;
  private final Cache cache;

  /**
   * Instantiates a new Location service.
   *
   * @param locationrepository the locationrepository
   * @param ipRepository the ip repository
   * @param tagRepository the tag repository
   * @param cache the cache
   */
  public LocationService(
      LocationRepository locationrepository,
      IpRepository ipRepository,
      TagRepository tagRepository,
      Cache cache) {
    this.locationRepository = locationrepository;
    this.ipRepository = ipRepository;
    this.tagRepository = tagRepository;
    this.cache = cache;
  }

  private Location convertToLocation(LocationDto locationDto) {
    Location location = new Location();
    location.setCountry(locationDto.country());
    location.setCity(locationDto.city());

    List<Tag> tags = tagRepository.findAllById(locationDto.tags());
    location.setTags(tags);

    return location;
  }

  /**
   * Create locations list.
   *
   * @param locationDtos the location dtos
   * @return the list
   */
  public List<Long> createLocations(List<LocationDto> locationDtos) {
    List<Location> locations = locationDtos.stream().map(this::convertToLocation).toList();
    locationRepository.saveAll(locations);
    return locations.stream().map(Location::getId).toList();
  }

  /**
   * Gets all locations.
   *
   * @return the all locations
   */
  public List<Location> getAllLocations() {
    return locationRepository.findAll();
  }

  /**
   * Gets location by id.
   *
   * @param id the id
   * @return the location by id
   */
  public Location getLocationById(Long id) {
    if (cache.containsKey(CACHE_KEY + id)) {
      return (Location) cache.getFromCache(CACHE_KEY + id);
    }
    Location location =
        locationRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    cache.putToCache(CACHE_KEY + id, location);
    return location;
  }

  /**
   * Gets location by ip.
   *
   * @param address the address
   * @return the location by ip
   */
  public Location getLocationByIp(String address) {
    return locationRepository.findLocationByIp(address);
  }

  /**
   * Gets location by tag.
   *
   * @param name the name
   * @return the location by tag
   */
  public List<Location> getLocationByTag(String name) {
    return locationRepository.findLocationByTag(name);
  }

  /**
   * Create location long.
   *
   * @param country the country
   * @param city the city
   * @param tagsIds the tags ids
   * @return the long
   */
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

  /**
   * Update location.
   *
   * @param id the id
   * @param country the country
   * @param city the city
   * @param ids the ids
   */
  public Long updateLocation(Long id, String country, String city, Map<String, List<Long>> ids) {
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
    List<Long> ips = ids.get("ips");
    if (!ips.isEmpty()) {
      List<Ip> ipList = new ArrayList<>();
      for (Long ipsId : ips) {
        ipList.add(ipRepository.getIpById(ipsId));
        ipRepository.getIpById(ipsId).setLocation(location);
      }
      location.setIps(ipList);
    }
    List<Long> tags = ids.get("tags");
    if (!tags.isEmpty()) {
      List<Tag> tagList = new ArrayList<>();
      for (Long tagsId : tags) {
        tagList.add(tagRepository.getTagById(tagsId));
      }
      location.setTags(tagList);
    }
    locationRepository.save(location);
    cache.putToCache(CACHE_KEY + id, location);
    return location.getId();
  }

  /**
   * Delete location by id.
   *
   * @param id the id
   */
  public void deleteLocationById(Long id) {
    locationRepository.deleteById(id);
    cache.removeFromCache(CACHE_KEY + id);
  }

  /**
   * Add ip to location.
   *
   * @param locationId the location id
   * @param ipId the ip id
   */
  public void addIpToLocation(Long locationId, Long ipId) {
    Ip ip = ipRepository.getIpById(ipId);
    Location location = getLocationById(locationId);
    if (location != null && ip != null) {
      ip.setLocation(location);
      location.getIps().add(ip);
      locationRepository.save(location);
      cache.putToCache(CACHE_KEY + locationId, location);
    } else {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Add tag to location.
   *
   * @param locationId the location id
   * @param tagId the tag id
   */
  public void addTagToLocation(Long locationId, Long tagId) {
    Tag tag = tagRepository.getTagById(tagId);
    Location location = getLocationById(locationId);
    if (location != null && tag != null) {
      tag.getLocations().add(location);
      locationRepository.save(location);
      cache.putToCache(CACHE_KEY + locationId, location);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }
}
