package com.example.locationip.controller;

import com.example.locationip.model.Tag;
import com.example.locationip.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<String> createIP(@RequestBody Tag tag) {
        if (tagService.existsById(tag.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tag with the given ID already exists.");
        }
        tagService.saveTag(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tag created successfully.");
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        Tag oldTag = getTagById(id);
        tag.setId(id);
        tag.setLocations(oldTag.getLocations());
        return tagService.saveTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTag(@PathVariable Long id) {
        if (!tagService.getTagById(id).getLocations().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tag is used. Delete only unused tags");
        }
        tagService.deleteTag(id);
        return ResponseEntity.ok("Tag was deleted");
    }
}
