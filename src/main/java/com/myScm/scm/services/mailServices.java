package com.myScm.scm.services;

import org.springframework.web.multipart.MultipartFile;


public interface mailServices {


    boolean SendMail(String to, String from, String subject, String message, MultipartFile file , String contactName , String userName );
    boolean verifyMail(String to);
    boolean verifyMailforchange(String to , String currentMail);
    boolean sendFeedback(String to , String email , String phone, String query);
    boolean sendMailforChangePass(String to);
}
