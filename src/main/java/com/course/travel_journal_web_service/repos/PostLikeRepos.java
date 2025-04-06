package com.course.travel_journal_web_service.repos;

import com.course.travel_journal_web_service.models.PostLike;
import com.course.travel_journal_web_service.models.User;
import com.course.travel_journal_web_service.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepos extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
