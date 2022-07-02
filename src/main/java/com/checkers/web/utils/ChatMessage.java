package com.checkers.web.utils;

import java.time.LocalDateTime;

public class ChatMessage {

    private String from;
    private LocalDateTime time;
    private String message;

    public ChatMessage(String from, String message) {
        this.from = from;
        this.time = LocalDateTime.now();
        this.message = message;
    }


    public String getFrom() {
        return from;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }
}
