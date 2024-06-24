package com.myScm.scm.serviceImpl;

import com.myScm.scm.errorHandler.EmailMessageingException;
import com.myScm.scm.helper.AppConstants;
import java.util.Date;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myScm.scm.services.mailServices;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements mailServices {

    private JavaMailSender javaMailSender;
    private MimeMessage mimeMessage;
    private MimeMessageHelper messageHelper;
    private String content = "";

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @SuppressWarnings("null")
    @Override
    public boolean SendMail(String to, String from, String subject, String message, MultipartFile file,
            String contactName, String userName) {

        mimeMessage = javaMailSender.createMimeMessage();

        try {

            // HTML content
            content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7;}" +
                    "        .container {padding: 20px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 20px auto;}"
                    +
                    "        .header {font-size: 24px; font-weight: bold; margin-bottom: 20px;}" +
                    "        .content {font-size: 16px; line-height: 1.6;}" +
                    "        .footer {font-size: 12px; text-align: right; margin-top: 20px; color: #888888;}" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='header'>Dear " + contactName + ",</div>" +
                    "        <div class='content'>" +
                    "            <p>" + userName + " has sent you a message regarding: <strong>" + subject
                    + "</strong></p>" +
                    "            <p class='info'><strong>Message:</strong></p>" +

                    "            <p>" + message + "</p>" +
                    "        </div>" +
                    "        <div class='footer'>SCM2@gmail.com</div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            messageHelper.setTo(to);
            messageHelper.setSubject("from SCM2");
            messageHelper.setText(content, true);
            if (file.getSize() > 10) {

                messageHelper.addAttachment(file.getOriginalFilename(), file);
            }

            messageHelper.setSentDate(new Date());
            messageHelper.setFrom(from);

            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {

            return false;
        }

    }

    @Override
    public boolean verifyMail(String to) {

        mimeMessage = javaMailSender.createMimeMessage();

        try {

            content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7;}" +
                    "        .container {padding: 20px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 20px auto;}"
                    +
                    "        .header {font-size: 24px; font-weight: bold; margin-bottom: 20px;}" +
                    "        .content {font-size: 16px; line-height: 1.6;}" +
                    "        .footer {font-size: 12px; text-align: right; margin-top: 20px; color: #888888;}" +
                    "        .button {display: inline-block; padding: 10px 20px; margin-top: 20px; font-size: 16px; color: #ffffff; background-color: #007BFF; text-decoration: none; border-radius: 5px;}"
                    +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='header'>Email Verification</div>" +
                    "        <div class='content'>" +
                    "            <p>Thank you for registering. To complete the process, please verify your email address by clicking the link below.</p>"
                    +
                    "            <p><a href=" + AppConstants.VERIFY_URL
                    + "scm2/verify/email/link/user/byuser/dkhefuen-eefefe?email=" + to
                    + "><h3>Verify Email</h3></a></p>" +
                    "            <p>This link will expire in 5 minutes.</p>" +
                    "        </div>" +
                    "        <div class='footer'>SCM2@gmail.com</div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

            messageHelper.setTo(to);
            messageHelper.setSubject("Verify-Mail");
            messageHelper.setText(content, true);

            messageHelper.setSentDate(new Date());

            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            return false;
        }

    }

    // Send Feedback

    @Override
    public boolean sendFeedback(String to, String email, String phone, String query){

        this.mimeMessage = this.javaMailSender.createMimeMessage();

        try {

            content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7;}" +
                    "        .container {padding: 20px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 20px auto;}"
                    +
                    "        .header {font-size: 24px; font-weight: bold; margin-bottom: 20px;}" +
                    "        .content {font-size: 16px; line-height: 1.6;}" +
                    "        .footer {font-size: 12px; text-align: right; margin-top: 20px; color: #888888;}" +
                    "        .info {margin-bottom: 10px;}" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='header'>Feedback</div>" +
                    "        <div class='content'>" +
                    "            <p class='info'><strong>Email:</strong> " + email + "</p>" +
                    "            <p class='info'><strong>Mobile Number:</strong> " + phone + "</p>" +
                    "            <p class='info'><strong>Feedback:</strong></p>" +
                    "            <p>" + query + "</p>" +
                    "        </div>" +
                    "        <div class='footer'>SCM2@gmail.com</div>" +
                    "    </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            this.messageHelper = new MimeMessageHelper(this.mimeMessage, "UTF-8");
            messageHelper.setTo(to);
            messageHelper.setSubject("Feedback From the SCM2");
            messageHelper.setText(content, true);

            javaMailSender.send(mimeMessage);

            return true;
        } catch (Exception exception) {
            throw new EmailMessageingException(
                    "INTERNAL_SERVER_ERROR for sending the feedback so try again after some time!!");
        }

    }

    @Override
    public boolean verifyMailforchange(String to, String currentMail) {

        mimeMessage = javaMailSender.createMimeMessage();

        try {

            content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7;}" +
                    "        .container {padding: 20px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 20px auto;}"
                    +
                    "        .header {font-size: 24px; font-weight: bold; margin-bottom: 20px;}" +
                    "        .content {font-size: 16px; line-height: 1.6;}" +
                    "        .footer {font-size: 12px; text-align: right; margin-top: 20px; color: #888888;}" +
                    "        .button {display: inline-block; padding: 10px 20px; margin-top: 20px; font-size: 16px; color: #ffffff; background-color: #007BFF; text-decoration: none; border-radius: 5px;}"
                    +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='header'>Email Verification</div>" +
                    "        <div class='content'>" +
                    "            <p>This mail is send for the purpose to <h2>Change Email</h2> to click the below link for verify the email.</p>"
                    +
                    "            <p><a href=" + AppConstants.VERIFY_URL
                    + "scm2/user/change/verify/email/link/user/byuser?email=" + to + "&cMail=" + currentMail
                    + "><h3>Verify Email</h3></a></p>" +
                    "            <p>This link will expire in 5 minutes.</p>" +
                    "        </div>" +
                    "        <div class='footer'>SCM2@gmail.com</div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

            messageHelper.setTo(to);
            messageHelper.setSubject("Verify-Mail");
            messageHelper.setText(content, true);

            messageHelper.setSentDate(new Date());

            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            return false;
        }

    }

    @Override
    public boolean sendMailforChangePass(String to) {

        mimeMessage = javaMailSender.createMimeMessage();

        try {

            content = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        body {font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f7f7f7;}" +
                    "        .container {padding: 20px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); max-width: 600px; margin: 20px auto;}"
                    +
                    "        .header {font-size: 24px; font-weight: bold; margin-bottom: 20px;}" +
                    "        .content {font-size: 16px; line-height: 1.6;}" +
                    "        .footer {font-size: 12px; text-align: right; margin-top: 20px; color: #888888;}" +
                    "        .button {display: inline-block; padding: 10px 20px; margin-top: 20px; font-size: 16px; color: #ffffff; background-color: #007BFF; text-decoration: none; border-radius: 5px;}"
                    +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='header'>Email Verification</div>" +
                    "        <div class='content'>" +
                    "            <p>This mail is send for the purpose <h3>Change Password</h3> to click the below link and change your password.</p>"
                    +
                    "            <p><a href=" + AppConstants.VERIFY_URL + "scm2/linkClicked/" + AppConstants.CHANGE_URL
                    + "?email=" + to
                    + "><h3>change</h3></a></p>" +
                    "            <p>This link will expire in 5 minutes.</p>" +
                    "        </div>" +
                    "        <div class='footer'>SCM2@gmail.com</div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

            messageHelper.setTo(to);
            messageHelper.setSubject("Change-Password");
            messageHelper.setText(content, true);

            messageHelper.setSentDate(new Date());

            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            return false;
        }

    }

}
