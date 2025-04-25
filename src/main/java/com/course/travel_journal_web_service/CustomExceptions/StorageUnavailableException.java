package com.course.travel_journal_web_service.CustomExceptions;

public class StorageUnavailableException extends RuntimeException {
    public StorageUnavailableException(String message) {
        super(message);
    }
}
