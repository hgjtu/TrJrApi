package com.course.travel_journal_web_service.repos;

import com.course.travel_journal_web_service.models.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepos extends JpaRepository<PostLike, Long> {
}
