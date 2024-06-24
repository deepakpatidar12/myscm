package com.myScm.scm;

import org.springframework.boot.SpringApplication;

import io.github.cdimascio.dotenv.Dotenv;

public class Application {

        public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        System.setProperty("GOOGLE_CLIENT_ID", dotenv.get("GOOGLE_CLIENT_ID"));
        System.setProperty("GOOGLE_CLIENT_SECRET", dotenv.get("GOOGLE_CLIENT_SECRET"));
        
        System.setProperty("GITHUB_CLIENT_ID", dotenv.get("GITHUB_CLIENT_SECRET"));
        System.setProperty("GITHUB_CLIENT_SECRET", dotenv.get("GITHUB_CLIENT_SECRET"));
     
        System.setProperty("MAIL_SENDER_ID", dotenv.get("MAIL_SENDER_ID"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        
        SpringApplication.run(Application.class, args);
    }
}
