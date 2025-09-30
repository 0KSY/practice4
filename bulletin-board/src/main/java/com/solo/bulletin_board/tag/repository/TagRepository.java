package com.solo.bulletin_board.tag.repository;

import com.solo.bulletin_board.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
