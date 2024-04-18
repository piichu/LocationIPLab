package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.Ip;
import com.example.locationip.model.Location;
import com.example.locationip.model.Tag;
import com.example.locationip.repository.IpRepository;
import com.example.locationip.repository.LocationDto;
import com.example.locationip.repository.LocationRepository;
import com.example.locationip.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LocationServiceTest {
  @InjectMocks LocationService locationService;
  @Mock IpRepository ipRepository;
  @Mock TagRepository tagRepository;
  @Mock Cache cache;
  @Mock LocationRepository locationRepository;
  @Mock LocationDto locationDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testConvertToLocation() {
    when(locationDto.country()).thenReturn("Country");
    when(locationDto.city()).thenReturn("City");
    List<Long> tagIds = Arrays.asList(1L, 2L, 3L);
    when(locationDto.tags()).thenReturn(tagIds);
    Tag tag1 = new Tag();
    tag1.setId(1L);
    Tag tag2 = new Tag();
    tag2.setId(2L);
    Tag tag3 = new Tag();
    tag3.setId(3L);
    when(tagRepository.findAllById(tagIds)).thenReturn(Arrays.asList(tag1, tag2, tag3));
    Location result = locationService.convertToLocation(locationDto);
    assertEquals("Country", result.getCountry());
    assertEquals("City", result.getCity());
    assertEquals(Arrays.asList(tag1, tag2, tag3), result.getTags());
  }

  @Test
  void testCreateLocations() {
    List<LocationDto> locationDtos =
        Arrays.asList(new LocationDto("1", "1", null), new LocationDto("2", "2", null));
    List<Location> expectedLocations =
        Arrays.asList(
            new Location(null, "1", "1", new ArrayList<>(), new ArrayList<>()),
            new Location(null, "2", "2", new ArrayList<>(), new ArrayList<>()));
    when(locationRepository.saveAll(anyList())).thenReturn(expectedLocations);
    List<Long> result = locationService.createLocations(locationDtos);
    verify(locationRepository, times(1)).saveAll(expectedLocations);
    List<Long> expectedIds =
        Arrays.asList(expectedLocations.get(0).getId(), expectedLocations.get(1).getId());
    assertEquals(expectedIds, result);
  }

  @Test
  void testGetAllLocations() {
    List<Location> locations = Arrays.asList(new Location(), new Location());
    when(locationRepository.findAll()).thenReturn(locations);
    List<Location> result = locationService.getAllLocations();
    verify(locationRepository, times(1)).findAll();
    assertEquals(locations, result);
  }

  @Test
  void testGetLocationById_CacheHit() {
    Location location = new Location();
    location.setId(1L);
    when(cache.containsKey("location-1")).thenReturn(true);
    when(cache.getFromCache("location-1")).thenReturn(location);
    Location result = locationService.getLocationById(1L);
    verify(cache, times(1)).containsKey("location-1");
    verify(cache, times(1)).getFromCache("location-1");
    verify(locationRepository, never()).findById(1L);
    assertEquals(location, result);
  }

  @Test
  void testGetLocationById_CacheMiss() {
    Location location = new Location();
    location.setId(1L);
    when(cache.containsKey("location-1")).thenReturn(false);
    when(locationRepository.findById(1L)).thenReturn(java.util.Optional.of(location));
    Location result = locationService.getLocationById(1L);
    verify(cache, times(1)).containsKey("location-1");
    verify(cache, never()).getFromCache("location-1");
    verify(locationRepository, times(1)).findById(1L);
    verify(cache, times(1)).putToCache("location-1", location);
    assertEquals(location, result);
  }

  @Test
  void testGetLocationById_LocationNotFound() {
    when(cache.containsKey("location-1")).thenReturn(false);
    when(locationRepository.findById(1L)).thenReturn(java.util.Optional.empty());
    try {
      locationService.getLocationById(1L);
    } catch (ResponseStatusException e) {
      verify(cache, times(1)).containsKey("location-1");
      verify(cache, never()).getFromCache("location-1");
      verify(locationRepository, times(1)).findById(1L);
      verify(cache, never()).putToCache("location-1", mock(Location.class));
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
    }
  }

  @Test
  void testGetLocationByIp() {
    Location location = new Location();
    Ip ip = new Ip();
    ip.setAddress("192.168.0.1");
    location.getIps().add(ip);
    when(locationRepository.findLocationByIp("192.168.0.1")).thenReturn(location);
    Location result = locationService.getLocationByIp("192.168.0.1");
    verify(locationRepository, times(1)).findLocationByIp("192.168.0.1");
    assertEquals(location, result);
  }

  @Test
  void testGetLocationByTag() {
    List<Location> locations = Arrays.asList(new Location(), new Location());
    Tag tag = new Tag();
    tag.setName("123");
    locations.get(0).getTags().add(tag);
    when(locationRepository.findLocationByTag("123")).thenReturn(locations);
    List<Location> result = locationService.getLocationByTag("123");
    verify(locationRepository, times(1)).findLocationByTag("123");
    assertEquals(locations, result);
  }

  @Test
  void testCreateLocation() {
    String country = "Country 1";
    String city = "City 1";
    Location location = new Location();
    location.setCountry(country);
    location.setCity(city);
    List<Long> tagsIds = Arrays.asList(1L, 2L);
    List<Tag> tags = Arrays.asList(new Tag(), new Tag());
    tags.get(0).setId(1L);
    tags.get(1).setId(2L);
    location.setTags(tags);
    when(tagRepository.getTagById(1L)).thenReturn(tags.get(0));
    when(tagRepository.getTagById(2L)).thenReturn(tags.get(1));
    Long result = locationService.createLocation(country, city, tagsIds);
    verify(tagRepository, times(2)).getTagById(anyLong());
    verify(locationRepository, times(1)).save(location);
    verify(cache, times(1)).putToCache("location-" + location.getId(), location);
    assertEquals(location.getId(), result);
  }

  @Test
  void testCreateLocation_NullTags() {
    String country = "Country 1";
    String city = "City 1";
    Location location = new Location();
    location.setCountry(country);
    location.setCity(city);
    Long result = locationService.createLocation(country, city, null);
    verify(tagRepository, never()).getTagById(anyLong());
    verify(locationRepository, times(1)).save(location);
    verify(cache, times(1)).putToCache("location-" + location.getId(), location);
    assertEquals(location.getId(), result);
  }

  @Test
  void testUpdateLocation() {
    Long id = 1L;
    String country = "Country 1";
    String city = "City 1";
    Location location = new Location();
    location.setId(id);
    location.setCountry("Old Country");
    location.setCity("Old City");
    Map<String, List<Long>> ids = new HashMap<>();
    ids.put("ips", Arrays.asList(1L, 2L));
    ids.put("tags", Arrays.asList(3L, 4L));
    List<Ip> ips = Arrays.asList(new Ip(), new Ip());
    ips.get(0).setId(1L);
    ips.get(1).setId(2L);
    List<Tag> tags = Arrays.asList(new Tag(), new Tag());
    ips.get(0).setId(3L);
    ips.get(1).setId(4L);
    when(cache.containsKey("location-" + id)).thenReturn(true);
    when(cache.getFromCache("location-" + id)).thenReturn(location);
    when(ipRepository.getIpById(1L)).thenReturn(ips.get(0));
    when(ipRepository.getIpById(2L)).thenReturn(ips.get(1));
    when(tagRepository.getTagById(3L)).thenReturn(tags.get(0));
    when(tagRepository.getTagById(4L)).thenReturn(tags.get(1));
    Long result = locationService.updateLocation(id, country, city, ids);
    verify(cache, times(1)).containsKey("location-" + id);
    verify(cache, times(1)).getFromCache("location-" + id);
    verify(mock(LocationService.class), never()).getLocationById(id);
    verify(tagRepository, times(2)).getTagById(anyLong());
    verify(locationRepository, times(1)).save(location);
    verify(cache, times(1)).putToCache("location-" + id, location);
    assertEquals(location.getId(), result);
  }

  @Test
  void testDeleteLocationById() {
    Long id = 1L;
    doNothing().when(locationRepository).deleteById(id);
    doNothing().when(cache).removeFromCache("location-" + id);
    locationService.deleteLocationById(id);
    verify(locationRepository, times(1)).deleteById(id);
    verify(cache, times(1)).removeFromCache("location-" + id);
  }

  @Test
  void testAddIpToLocation() {
    Long locationId = 1L;
    Long ipId = 1L;
    Ip ip = new Ip();
    ip.setId(ipId);
    Location location = new Location();
    location.setId(locationId);
    when(ipRepository.getIpById(ipId)).thenReturn(ip);
    when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
    locationService.addIpToLocation(locationId, ipId);
    verify(locationRepository, times(1)).save(location);
    verify(cache, times(2)).putToCache(eq("location-" + locationId), any());
  }

  @Test
  void testAddIpToLocation_WhenLocationOrIpIsNull_ShouldThrowException() {
    Long locationId = 1L;
    Long ipId = 1L;
    when(ipRepository.getIpById(ipId)).thenReturn(null);
    when(locationService.getLocationById(locationId)).thenReturn(null);
    assertThrows(
        NullPointerException.class,
        () -> {
          locationService.addIpToLocation(locationId, ipId);
        });
  }

  @Test
  void testAddTagToLocation() {
    Long locationId = 1L;
    Long tagId = 1L;
    Tag tag = new Tag();
    tag.setId(tagId);
    Location location = new Location();
    location.setId(locationId);
    when(tagRepository.getTagById(tagId)).thenReturn(tag);
    when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
    locationService.addTagToLocation(locationId, tagId);
    verify(locationRepository, times(1)).save(location);
    verify(cache, times(2)).putToCache(eq("location-" + locationId), any());
  }

  @Test
  void testAddTagToLocation_WhenLocationOrTagIsNull_ShouldThrowException() {
    Long locationId = 1L;
    Long tagId = 1L;
    when(tagRepository.getTagById(tagId)).thenReturn(null);
    when(locationService.getLocationById(locationId)).thenReturn(null);
    assertThrows(
        NullPointerException.class,
        () -> {
          locationService.addTagToLocation(locationId, tagId);
        });
  }
}
