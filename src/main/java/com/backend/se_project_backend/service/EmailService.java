package com.backend.se_project_backend.service;

import javax.mail.MessagingException;

public interface EmailService {
    void send(String to, String subject_text, String content_text) throws MessagingException;
}
