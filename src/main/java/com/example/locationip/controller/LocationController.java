package com.example.locationip.controller;

import com.example.locationip.model.Location;
import com.example.locationip.service.LocationService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** The type Location controller. */
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
  public Location getLocationByIp(@RequestParam String address) {
    return locationService.getLocationByIp(address);
  }

  @GetMapping("/tag")
  public List<Location> getLocationByTag(@RequestParam String name) {
    return locationService.getLocationByTag(name);
  }

  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping()
  public Long createLocation(
      @RequestParam String country,
      @RequestParam String city,
      @RequestBody(required = false) List<Long> tags) {
    return locationService.createLocation(country, city, tags);
  }

  /**
   * Update location.
   *
   * @param id the id
   * @param country the country
   * @param city the city
   * @param ids the ids
   */
  @Transactional
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{id}")
  public Long updateLocation(
      @PathVariable Long id,
      @RequestParam(required = false) String country,
      @RequestParam(required = false) String city,
      @RequestBody(required = false) Map<String, List<Long>> ids) {
    List<Long> ips = ids.get("ips");
    List<Long> tags = ids.get("tags");
    locationService.updateLocation(id, country, city, ips, tags);
    return id;
  }

  @Transactional
  @DeleteMapping("/{id}")
  public Long deleteLocation(@PathVariable Long id) {
    locationService.deleteLocationById(id);
    return id;
  }

  @Transactional
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/addIp")
  public void addIpToLocation(@RequestParam Long locationId, @RequestParam Long ipId) {
    locationService.addIpToLocation(locationId, ipId);
  }

  @Transactional
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/addTag")
  public void addTagToLocation(@RequestParam Long locationId, @RequestParam Long tagId) {
    locationService.addTagToLocation(locationId, tagId);
  }
}
