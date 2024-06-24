/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.myScm.scm.customValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import com.myScm.scm.helper.AppConstants;


public class ImageSizeValidator implements ConstraintValidator<imageSize, MultipartFile>{

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

        return value.getSize()<= AppConstants.IMAGE_SIZE;

    }
    
    
    
}
