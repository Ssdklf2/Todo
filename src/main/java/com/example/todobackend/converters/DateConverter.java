package com.example.todobackend.converters;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class DateConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime getTimeFromString(String string) throws DateTimeParseException {
        return LocalDateTime.parse(string, formatter);
    }

    public String getStringFromTime(LocalDateTime time) {
        return time.format(formatter);
    }
}
