package com.course.travel_journal_web_service.repos;

import com.course.travel_journal_web_service.models.TestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepos extends JpaRepository<Integer, TestModel> {
    public TestModel findById(Integer id);
}
