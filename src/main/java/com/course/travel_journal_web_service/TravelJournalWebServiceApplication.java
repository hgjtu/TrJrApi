package com.course.travel_journal_web_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class TravelJournalWebServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelJournalWebServiceApplication.class, args);
	}

}
