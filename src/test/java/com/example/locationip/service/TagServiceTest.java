package com.example.locationip.service;

import com.example.locationip.component.Cache;
import com.example.locationip.model.Location;
import com.example.locationip.model.Tag;
import com.example.locationip.repository.LocationRepository;
import com.example.locationip.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceTest {
  @InjectMocks TagService tagService;
  @Mock TagRepository tagRepository;
  @Mock LocationRepository locationRepository;
  @Mock Cache cache;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void testGetTagById() {
    Long id = 1L;
    Tag tag = new Tag();
    tag.setId(id);
    when(tagRepository.findById(id)).thenReturn(Optional.of(tag));
    when(cache.containsKey("tag-" + id)).thenReturn(true);
    when(cache.getFromCache("tag-" + id)).thenReturn(tag);
    Tag result = tagService.getTagById(id);
    assertEquals(tag, result);
    verify(tagRepository, never()).findById(id);
    when(cache.containsKey("tag-" + id)).thenReturn(false);
    result = tagService.getTagById(id);
    assertEquals(tag, result);
    verify(tagRepository, times(1)).findById(id);
    verify(cache, times(1)).putToCache("tag-" + id, tag);
  }

  @Test
  void testGetAllTags() {
    List<Tag> tags = Arrays.asList(new Tag(), new Tag());
    when(tagRepository.findAll()).thenReturn(tags);
    List<Tag> result = tagService.getAllTags();
    assertEquals(result, tags);
    verify(tagRepository, times(1)).findAll();
  }

  @Test
  void testCreateTag() {
    String tagName = "New Tag";
    List<Long> locationIds = Arrays.asList(1L, 2L, 3L);
    when(locationRepository.getLocationById(any(Long.class))).thenReturn(new Location());
    Long createdTagId = tagService.createTag(tagName, locationIds);
    ArgumentCaptor<Tag> tagCaptor = ArgumentCaptor.forClass(Tag.class);
    verify(tagRepository, times(1)).save(tagCaptor.capture());
    Tag savedTag = tagCaptor.getValue();
    verify(cache, times(1)).putToCache(eq("tag-" + createdTagId), any(Tag.class));
    assertEquals(createdTagId, savedTag.getId());
  }

  @Test
  void testUpdateTag() {
    Long tagId = 1L;
    String updatedName = "Updated Tag";
    List<Long> updatedLocationIds = Arrays.asList(1L, 2L, 3L);
    Tag existingTag = new Tag();
    existingTag.setId(tagId);
    existingTag.setName("existing");
    when(tagRepository.getTagById(tagId)).thenReturn(existingTag);
    List<Location> updatedLocations = Arrays.asList(new Location(), new Location(), new Location());
    when(locationRepository.getLocationById(any(Long.class))).thenReturn(new Location());
    when(locationRepository.getLocationById(1L)).thenReturn(updatedLocations.get(0));
    when(locationRepository.getLocationById(2L)).thenReturn(updatedLocations.get(1));
    when(locationRepository.getLocationById(3L)).thenReturn(updatedLocations.get(2));
    tagService.updateTag(tagId, updatedName, updatedLocationIds);
    if (cache.containsKey("tag-" + tagId)) {
      verify(cache, times(1)).getFromCache("tag-" + tagId);
    } else {
      verify(tagRepository, times(1)).getTagById(tagId);
    }
    assertEquals(updatedName, existingTag.getName());
    assertEquals(updatedLocations, existingTag.getLocations());
    verify(tagRepository, times(1)).save(existingTag);
    verify(cache, times(1)).putToCache("tag-" + tagId, existingTag);
  }

  @Test
  void testDeleteTagById() {
    Long tagId = 1L;
    tagService.deleteTagById(tagId);
    verify(tagRepository, times(1)).deleteById(tagId);
    verify(cache, times(1)).removeFromCache("tag-" + tagId);
  }

  @Test
  void testAddLocationToTag() {
    Long tagId = 1L;
    Long locationId = 1L;
    Tag tag = new Tag();
    tag.setId(tagId);
    when(tagRepository.getTagById(tagId)).thenReturn(tag);
    Location location = new Location();
    location.setId(locationId);
    when(locationRepository.getLocationById(locationId)).thenReturn(location);
    tagService.addLocationToTag(tagId, locationId);
    verify(locationRepository, times(1)).getLocationById(locationId);
    verify(tagRepository, times(1)).getTagById(tagId);
    assertTrue(tag.getLocations().contains(location));
    verify(tagRepository, times(1)).save(tag);
    verify(cache, times(1)).putToCache("tag-" + tagId, tag);
  }

  @Test
  void testAddLocationToTagWithNullTagOrLocation() {
    Long tagId = 1L;
    Long locationId = 1L;
    when(tagRepository.getTagById(tagId)).thenReturn(null);
    when(locationRepository.getLocationById(locationId)).thenReturn(null);
    assertThrows(
        ResponseStatusException.class, () -> tagService.addLocationToTag(tagId, locationId));
  }
}
