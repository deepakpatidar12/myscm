package com.myScm.scm.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.myScm.scm.helper.AppConstants;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSender geJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.debug", true);

        mailSender.setUsername(AppConstants.SENDER_MAIL);
        mailSender.setPassword(AppConstants.MAIL_PASSWORD);

        return mailSender;

    }

}
