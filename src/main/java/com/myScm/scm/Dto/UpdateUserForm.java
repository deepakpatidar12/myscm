package com.myScm.scm.Dto;

import org.springframework.web.multipart.MultipartFile;

import com.myScm.scm.customValidator.imageSize;
import com.myScm.scm.customValidator.imageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserForm {

    @NotBlank(message = "user name is not empty!!")
    @Size(min = 3, max = 15)
    private String userName;

    @Size(min = 10, max = 10, message = "enter only 10 digit!!")
    @NotBlank(message = "mobile number not be empty!!")
    private String userNumber;

    @imageSize
    @imageType
    private MultipartFile profile;

    private String profilePic;

}
