package com.example.locationip.controller;

import com.example.locationip.model.Tag;
import com.example.locationip.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagControllerTest {

  @Mock private TagService tagService;

  @InjectMocks private TagController tagController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllTags() {
    List<Tag> tags = Arrays.asList(new Tag(), new Tag());
    when(tagService.getAllTags()).thenReturn(tags);
    List<Tag> result = tagController.getAllTags();
    verify(tagService, times(1)).getAllTags();
    assertEquals(tags, result);
  }

  @Test
  void testGetTagById() {
    Long tagId = 1L;
    Tag tag = new Tag();
    tag.setId(tagId);
    when(tagService.getTagById(tagId)).thenReturn(tag);
    Tag result = tagController.getTagById(tagId);
    verify(tagService, times(1)).getTagById(tagId);
    assertEquals(tag, result);
  }

  @Test
  void testCreateTag() {
    String tagName = "Tag1";
    List<Long> locationIds = Arrays.asList(1L, 2L);
    Long createdTagId = 1L;
    when(tagService.createTag(tagName, locationIds)).thenReturn(createdTagId);
    Long result = tagController.createTag(tagName, locationIds);
    verify(tagService, times(1)).createTag(tagName, locationIds);
    assertEquals(createdTagId, result);
  }

  @Test
  void testUpdateTag() {
    Long tagId = 1L;
    String newName = "UpdatedTag";
    List<Long> newLocationIds = Arrays.asList(3L, 4L);
    Long result = tagController.updateTag(tagId, newLocationIds, newName);
    verify(tagService, times(1)).updateTag(tagId, newName, newLocationIds);
    assertEquals(tagId, result);
  }

  @Test
  void testAddLocationToTag() {
    Long tagId = 1L;
    Long locationId = 2L;
    doNothing().when(tagService).addLocationToTag(tagId, locationId);
    assertDoesNotThrow(() -> tagController.addLocationToTag(tagId, locationId));
    verify(tagService, times(1)).addLocationToTag(tagId, locationId);
  }

  @Test
  void testDeleteTag() {
    Long tagId = 1L;
    Long result = tagController.deleteTag(tagId);
    verify(tagService, times(1)).deleteTagById(tagId);
    assertEquals(tagId, result);
  }
}
