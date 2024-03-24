package com.example.locationip.controller;

import com.example.locationip.model.Tag;
import com.example.locationip.service.TagService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public Long createTag(@RequestParam String name, @RequestBody(required = false) List<Long> locations) {
        return tagService.createTag(name, locations);
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateTag(@PathVariable Long id,
                          @RequestBody(required = false) List<Long> locations,
                          @RequestParam(required = false) String name) {
        tagService.updateTag(id, name, locations);
    }

    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/addLocation")
    public void addLocationToTag(@RequestParam Long tagId, @RequestParam Long locationId) {
        tagService.addLocationToTag(tagId, locationId);
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTagById(id);
    }
}
