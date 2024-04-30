package com.activedge.usermgt.controller.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

    @Component
    public class EmailUtil {
        @Autowired
        private JavaMailSender javaMailSender;
        @Value("${introspec.auth_backend_url}")
        private String baseUrl;
        public EmailUtil(JavaMailSender emailSender) {
            this.javaMailSender = emailSender;
        }
        public void sendSetPassword(String email,String subject) throws MessagingException {
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
            String resetUrl = baseUrl + "/reset-password?email="+email;
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(String.format("<div><a href=\"%s\" target=\"_blank\">Click link to set password</a></div>", resetUrl), true);
            javaMailSender.send(mimeMessage);
        }
    }