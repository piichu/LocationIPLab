package com.example.locationip.controller;

import com.example.locationip.model.Tag;
import com.example.locationip.service.TagService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
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

/** The type Tag controller. */
@RestController
@CrossOrigin
@RequestMapping("/tags")
public class TagController {
  private final TagService tagService;

  @Autowired
  TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public List<Tag> getAllTags() {
    return tagService.getAllTags();
  }

  @GetMapping("/{id}")
  public Tag getTagById(@PathVariable Long id) {
    return tagService.getTagById(id);
  }

  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public Long createTag(
      @RequestParam String name, @RequestBody(required = false) List<Long> locations) {
    return tagService.createTag(name, locations);
  }

  @Transactional
  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{id}")
  public Long updateTag(
      @PathVariable Long id,
      @RequestBody(required = false) List<Long> locations,
      @RequestParam(required = false) String name) {
    tagService.updateTag(id, name, locations);
    return id;
  }

  @Transactional
  @ResponseStatus(HttpStatus.OK)
  @PatchMapping("/addLocation")
  public void addLocationToTag(@RequestParam Long tagId, @RequestParam Long locationId) {
    tagService.addLocationToTag(tagId, locationId);
  }

  @Transactional
  @DeleteMapping("/{id}")
  public Long deleteTag(@PathVariable Long id) {
    tagService.deleteTagById(id);
    return id;
  }
}
