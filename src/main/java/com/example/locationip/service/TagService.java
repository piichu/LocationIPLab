package com.example.locationip.service;

import com.example.locationip.model.Tag;
import com.example.locationip.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return tagRepository.existsById(id);
    }
}
