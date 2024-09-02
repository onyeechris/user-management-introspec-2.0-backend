package com.activedge.usermgt.controller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Base64;

@Component
    public class EmailUtil {
        @Autowired
        private JavaMailSender javaMailSender;
        @Value("${introspec.auth_backend_url}")
        private String baseUrl;
        @Autowired
        private BCryptPasswordEncoder encoder;
        public EmailUtil(JavaMailSender emailSender) {
            this.javaMailSender = emailSender;
        }
        public void sendSetPassword(String email, String subject) throws MessagingException {
            // Base64 encode the email to make it URL-safe
            String encodedEmail = Base64.getUrlEncoder().encodeToString(email.getBytes());

            // Construct the reset URL
            String resetUrl = baseUrl + "/reset-password?token=" + encodedEmail;

            // Create and send the email
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(String.format("<div><a href=\"%s\" target=\"_blank\">Click on link to set password</a></div>", resetUrl), true);
            javaMailSender.send(mimeMessage);
        }
    }