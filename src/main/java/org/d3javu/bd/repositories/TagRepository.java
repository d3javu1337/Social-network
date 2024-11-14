package org.d3javu.bd.repositories;

import org.d3javu.bd.models.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByBody(String name);



}
