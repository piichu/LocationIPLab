package com.example.locationip.repository;

import com.example.locationip.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** The interface Tag repository. */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
  Tag getTagById(Long id);
}
