package com.course.travel_journal_web_service.repos;

import com.course.travel_journal_web_service.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepos extends JpaRepository<Post, Long> {
}
