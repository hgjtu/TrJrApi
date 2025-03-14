package com.course.travel_journal_web_service.services;

import com.course.travel_journal_web_service.models.TestModel;
import com.course.travel_journal_web_service.repos.TestRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final TestRepos repos;

    @Autowired
    public TestService(TestRepos repos) {
        this.repos = repos;
    }

    public TestModel getTestModelById(Integer id) {
        return repos.findById(id).orElse(null);
    }
}
