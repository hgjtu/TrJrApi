package com.course.travel_journal_web_service.repos;

import com.course.travel_journal_web_service.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorRepos extends JpaRepository<User, Long> {

}