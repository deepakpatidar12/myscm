package com.myScm.scm.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.myScm.scm.Dto.UserForm;
import com.myScm.scm.entities.User;
import com.myScm.scm.helper.Message;

public interface userServices {

    User getUserFromLogin(Authentication  authentication);
    User saveUser(UserForm user)throws Exception;
    User getUserById(String id); 
    User updateUser(User user,String name , String number, MultipartFile file);
    void deleteUser(String id);
    boolean isUserExist(String user_id);
    boolean isUserByEmail(String email);
    List<User> getAllUser();
    boolean changeEmail(User user , String newMail);
    Message changePass(User user, String c_pass , String r_pass , String n_pass);
    Message forgotChangePass(User user , String newpass1 , String newpass2);
    boolean sendMail(String to, String from , String subject , String Message , MultipartFile file , String contactName , String userName);
    boolean sendFeedback(String to, String email, String phone , String query);
    boolean sendVerifyLink(String to);
    boolean sendChangeVerifyLink(String to , String currentMail);
    boolean verifyEmail(User user);
    boolean forgotPass(String email);
}
