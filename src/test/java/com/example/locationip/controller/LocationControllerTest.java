package com.example.locationip.controller;

import com.example.locationip.model.Ip;
import com.example.locationip.model.Location;
import com.example.locationip.model.Tag;
import com.example.locationip.repository.LocationDto;
import com.example.locationip.service.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocationControllerTest {
  @InjectMocks private LocationController locationController;
  @Mock private LocationService locationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testConstructor() {
    locationService.getAllLocations();
    verify(locationService).getAllLocations();
  }

  @Test
  void testGetAllLocations() {
    List<Location> locations =
        Arrays.asList(
            new Location(1L, "Spain", "Madrid", null, null),
            new Location(2L, "Spain", "Sevil", null, null));
    when(locationService.getAllLocations()).thenReturn(locations);
    List<Location> result = locationController.getAllLocations();
    assertEquals(locations, result);
  }

  @Test
  void testGetLocationById() {
    Location location = new Location(1L, "", "", null, null);
    when(locationService.getLocationById(1L)).thenReturn(location);
    Location result = locationController.getLocationById(1L);
    assertEquals(location, result);
  }

  @Test
  void testGetLocationByIp() {
    Location location = new Location();
    Ip ip = new Ip();
    String address = "1.1.1.1";
    ip.setAddress(address);
    location.getIps().add(ip);
    when(locationService.getLocationByIp(address)).thenReturn(location);
    Location result = locationController.getLocationByIp(address);
    assertEquals(result, location);
  }

  @Test
  void testGetLocationByTag() {
    List<Location> locations = Arrays.asList(new Location(), new Location(), new Location());
    String name = "test";
    Tag tag = new Tag();
    tag.setName(name);
    locations.get(0).getTags().add(tag);
    locations.get(1).getTags().add(new Tag());
    locations.get(2).getTags().add(tag);
    when(locationService.getLocationByTag(name)).thenReturn(locations);
    List<Location> result = locationController.getLocationByTag(name);
    assertEquals(result, locations);
  }

  @Test
  void testCreateLocation() {
    Long locationId = 1L;
    when(locationService.createLocation("", "", null)).thenReturn(locationId);
    Long result = locationController.createLocation("", "", null);
    assertEquals(result, locationId);
  }

  @Test
  void testCreateSeveralLocations() {
    List<Long> ids = Arrays.asList(1L, 2L);
    List<LocationDto> locationDtos =
        Arrays.asList(new LocationDto("", "", null), new LocationDto("2", "2", null));
    when(locationService.createLocations(locationDtos)).thenReturn(ids);
    List<Long> result = locationController.createSeveralLocations(locationDtos);
    assertEquals(result, ids);
  }

  @Test
  void testUpdateLocation() {
    Long locationId = 1L;
    String country = "Spain";
    String city = "Madrid";

    when(locationService.updateLocation(
            any(Long.class), any(String.class), any(String.class), anyMap()))
        .thenReturn(locationId);
    try {
      locationController.updateLocation(locationId, country, city, null);
    } catch (ResponseStatusException ex) {
      assertEquals(HttpStatus.CREATED, ex.getStatusCode());
    }
  }

  @Test
  void testDeleteLocation() {
    Long locationId = 1L;
    Long result = locationController.deleteLocation(locationId);
    verify(locationService, times(1)).deleteLocationById(locationId);
    assertEquals(locationId, result);
  }

  @Test
  void testAddIpToLocation() {
    Long locationId = 1L;
    Long ipId = 2L;
    doNothing().when(locationService).addIpToLocation(locationId, ipId);
    assertDoesNotThrow(() -> locationController.addIpToLocation(locationId, ipId));
    verify(locationService, times(1)).addIpToLocation(locationId, ipId);
  }

  @Test
  void testAddTagToLocation() {
    Long locationId = 1L;
    Long tagId = 2L;
    doNothing().when(locationService).addTagToLocation(locationId, tagId);
    assertDoesNotThrow(() -> locationController.addTagToLocation(locationId, tagId));
    verify(locationService, times(1)).addTagToLocation(locationId, tagId);
  }
}
