package com.backend.se_project_backend.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.backend.se_project_backend.utils.StringConstants.*;

@Service
@AllArgsConstructor
@Async
public class EmailServiceImpl implements EmailService{

    public final JavaMailSender javaMailSender;

    @Override
    public void send(String to, String subject_text, String content_text) throws MessagingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, "utf-8");
        mimeMessageHelper.setText(content_text);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject_text);
        mimeMessageHelper.setFrom(NO_REPLY_EMAIL_ADDRESS);
        javaMailSender.send(mimeMailMessage);
    }
}
