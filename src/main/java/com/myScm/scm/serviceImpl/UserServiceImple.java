package com.myScm.scm.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myScm.scm.Dto.ContactForm;
import com.myScm.scm.Dto.UserForm;
import com.myScm.scm.entities.Contact;
import com.myScm.scm.entities.User;
import com.myScm.scm.errorHandler.EmailMessageingException;
import com.myScm.scm.errorHandler.ResourceNotFoundException;
import com.myScm.scm.errorHandler.UnExpectedException;
import com.myScm.scm.errorHandler.UserNotFound;
import com.myScm.scm.errorHandler.UserValdationException;
import com.myScm.scm.helper.AppConstants;
import com.myScm.scm.helper.Helper;
import com.myScm.scm.helper.Message;
import com.myScm.scm.helper.MessageType;
import com.myScm.scm.repos.ContactRepo;
import com.myScm.scm.repos.UserRepo;
import com.myScm.scm.services.userServices;
import lombok.Getter;
import lombok.Setter;

@Service
@Setter
@Getter
public class UserServiceImple implements userServices {

    private final UserRepo userRepo;
    private final ContactRepo contactRepo;
    private final PasswordEncoder encoder;
    private final Helper helper;
    private User user;
    private UserForm userForm;
    private final CloudnairyServiceImpl cloudinaryService;
    private MailServiceImpl mailServiceImpl;

    public UserServiceImple(UserRepo userRepo, ContactRepo contactRepo, PasswordEncoder encoder, Helper helper,
            CloudnairyServiceImpl cloudnairyServiceImpl, MailServiceImpl mailServiceImpl) {
        this.userRepo = userRepo;
        this.contactRepo = contactRepo;
        this.encoder = encoder;
        this.helper = helper;
        this.cloudinaryService = cloudnairyServiceImpl;
        this.mailServiceImpl = mailServiceImpl;
    }

    // Get User By UserName
    public User getUserByUsername(String userName) {

        return this.userRepo.findByUserEmail(userName)
                .orElseThrow(() -> new UserNotFound("User not found for this email " + userName));

    }

    // Get UserForm by userName
    public UserForm getUserFormByUser(User user) {

        try {

            this.userForm = this.helper.convertUserForm(user);
            return userForm;
        } catch (UnExpectedException e) {
            throw new UnExpectedException("unexpected error occure during the processing the data!!");
        }
    }

    // Get User By UserForm
    public User getUserByUserFrom(UserForm userform) {

        try {

            this.user = this.helper.convertUser(userform);

            return this.user;
        } catch (UnExpectedException e) {
            throw new UnExpectedException("unexpected error occure during the processing the data!!");
        }
    }

    // Get All ContactForm from any User specific
    public List<ContactForm> getcontactForms(User user) {

        List<Contact> contacts = this.user.getContactList();

        List<ContactForm> contactForms = this.helper.convertAllContacts(contacts);

        return contactForms;
    }

    // Saving the User
    @Override
    public User saveUser(UserForm userform) throws Exception {

        try {

            // Convert UserForm to user
            this.user = helper.convertUser(userform);

            // Set User Id
            this.user.setUserId(UUID.randomUUID().toString());

            // Encode Password
            this.user.setPassword(this.encoder.encode(user.getPassword()));

            // Set Profile Pic
            user.setProfilePic("../../img/contact.png");

            return this.userRepo.save(user);
        } catch (UserValdationException e) {

            throw new UserValdationException("userdetail are not valid accordding to the requirement!!");
        }

    }

    // Get User By id
    @Override
    public User getUserById(String id) {

        return userRepo.findById(id).orElseThrow(() -> new UserNotFound("user not found !!"));

    }

    // Delete The User
    @Override
    public void deleteUser(String id) {

        try {

            this.user = this.userRepo.findById(id)
                    .orElseThrow(() -> new UserNotFound("User Not Present!!"));
            userRepo.delete(this.user);
        } catch (Exception e) {
            throw new UserNotFound("resources are not found for this user so we can't be delete this!!");
        }
    }

    // Is User Exist
    @Override
    public boolean isUserExist(String user_id) {

        try {

            this.user = userRepo.findById(user_id).orElse(null);

            return this.user != null ? true : false;
        } catch (Exception e) {

            throw new UserNotFound("we can't be find the resources for you");

        }

    }

    // Is User Exist Check By Email
    @Override
    public boolean isUserByEmail(String email) {

        try {

            this.user = userRepo.findByUserEmail(email).orElse(null);
            return this.user != null ? true : false;
        } catch (Exception e) {
            throw new UserNotFound("we can't find the resources for you");
        }
    }

    // Get All User
    @Override
    public List<User> getAllUser() {

        return userRepo.findAll();
    }

    // Update User Detail
    @Override
    public User updateUser(User user, String name, String number, MultipartFile file) {

        try {

            user.setUserName(name);
            user.setUserNumber(number);
            if (!file.isEmpty()) {

                String public_id = UUID.randomUUID().toString();
                String fileName = cloudinaryService.uploadOnCloud(file, public_id);
                user.setProfilePic(fileName);
                user.setProfilePublicId(public_id);
            }

            return this.userRepo.save(user);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(
                    "we can't find detail for you at this time so please try again some time after.");
        }

    }

    // Proceed The Change Password
    @Override
    public Message changePass(User user, String c_pass, String r_pass, String n_pass) {

        if (!this.encoder.matches(c_pass, user.getPassword())) {

            return new Message("current password does't match", MessageType.red);

        }

        if (r_pass.equals(n_pass)) {

            if (r_pass.length() >= 8 && r_pass.length() <= 12) {

                user.setPassword(this.encoder.encode(n_pass));
                this.userRepo.save(user);

                return new Message("password change successfully", MessageType.blue);
            } else {
                return new Message("length is 8-12 character", MessageType.red);

            }
        }

        return new Message("both password does't match", MessageType.red);

    }

    // Get The User From Login
    @SuppressWarnings("null")
    @Override
    public User getUserFromLogin(Authentication authentication) {

        try {

            if (authentication instanceof OAuth2AuthenticationToken) {

                var authToken = (OAuth2AuthenticationToken) authentication;
                var clientId = authToken.getAuthorizedClientRegistrationId();
                OAuth2User user = (OAuth2User) authentication.getPrincipal();

                // signin by google
                if (clientId.equalsIgnoreCase("google")) {

                    return userRepo.findByUserEmail(user.getAttribute("email").toString()).get();

                } else if (clientId.equalsIgnoreCase("github")) {
                    // signin by github

                    return userRepo
                            .findByUserEmail(user.getAttribute("email") != null ? user.getAttribute("email").toString()
                                    : user.getAttribute("login") + "@gmail.com")
                            .get();

                }
            } else {
                // signin by UserName and Password
                return userRepo.findByUserEmail(authentication.getName()).get();
            }

            return null;
        } catch (UserNotFound e) {
            throw new UserNotFound("we can't fetch the data for you so please try again after some time!");
        }

    }

    @Override
    public boolean sendMail(String to, String from, String subject, String Message, MultipartFile file,
            String contactName, String userName) {
        try {

            return mailServiceImpl.SendMail(to, from, subject, Message, file, contactName, userName);
        } catch (EmailMessageingException e) {

            throw new EmailMessageingException("please check your internet connection and email then send to the mail");
        }

    }

    @Override
    public boolean sendFeedback(String to, String email, String phone, String query) {

        try {

            return this.mailServiceImpl.sendFeedback(AppConstants.FEEDBACK_SENDER, email, phone, query);
        } catch (EmailMessageingException e) {
            throw new EmailMessageingException(
                    "INTERNAL_SERVER_ERROR for sending the feedback so try again after some time!!");
        }

    }

    // after tep the link to verify email
    @Override
    public boolean verifyEmail(User user) {

        try {

            user.setEmailValified(true);
            user.setEnabled(true);
            user.setNumberVarified(true);

            User mainUser = this.userRepo.save(user);

            return mainUser != null;
        } catch (UserValdationException e) {
            throw new UserValdationException(
                    "INTERNAL_SERVER_ERROR for the verifying the detail of you so try again after some time!!");
        }

    }

    // Send the link to the Email
    @Override
    public boolean sendVerifyLink(String to) {
        try {

            return this.mailServiceImpl.verifyMail(to);
        } catch (EmailMessageingException e) {
            throw new EmailMessageingException(
                    "INTERNAL_SERVER_ERROR for sending the verify link try again after some time ");
        }
    }

    @Override
    public boolean sendChangeVerifyLink(String to, String currentMail) {

        try {

            return this.mailServiceImpl.verifyMailforchange(to, currentMail);
        } catch (EmailMessageingException e) {
            throw new EmailMessageingException(
                    "INTERNAL_SERVER_ERROR for sending the verify link try again after some time ");

        }

    }

    @Override
    public boolean changeEmail(User user, String newMail) {

        try {

            user.setUserEmail(newMail);
            this.userRepo.save(user);
            return true;
        } catch (UserValdationException e) {
            throw new UserValdationException(
                    "INTERNAL_SERVER_ERROR for verify your email, so please try again after some time ");

        }

    }

    @Override
    public boolean forgotPass(String email) {

        try {

            this.user = userRepo.findByUserEmail(email).get();
            AppConstants.CHANGE_URL = UUID.randomUUID().toString();
            if (this.user != null) {

                this.mailServiceImpl.sendMailforChangePass(email);
                return true;
            }

        } catch (EmailMessageingException e) {
            return false;
        }
        return false;

    }

    // it is for change password
    @Override
    public Message forgotChangePass(User user, String newpass1, String newpass2) {

        try {

            if (newpass1.equals(newpass2)) {
                user.setPassword(this.encoder.encode(newpass2));
                this.userRepo.save(user);
                return new Message("password change successfull", MessageType.blue);
            } else if (!newpass1.equals(newpass2)) {

                return new Message("Both Password does't match !!", MessageType.red);
            }
            return new Message("INTERNAL_SERVER_ERROR try again later !!", MessageType.red);

        } catch (UserValdationException e) {
            throw new UserValdationException(
                    "we are not able to fatch the detail of this user and update the password so plase try again after some time!!");
        }

    }

}