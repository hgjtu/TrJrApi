package com.course.travel_journal_web_service.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum PostSort {
    DATE_ASC(Sort.by(Sort.Direction.ASC, "date")),
    DATE_DESC(Sort.by(Sort.Direction.DESC, "date")),

    LIKES_ASC(Sort.by(Sort.Direction.ASC, "likes")),
    LIKES_DESC(Sort.by(Sort.Direction.DESC, "likes")),

    STATUS_ASC(Sort.by(Sort.Direction.ASC, "status")),
    STATUS_DESC(Sort.by(Sort.Direction.DESC, "status"));

    private final Sort sortValue;

}