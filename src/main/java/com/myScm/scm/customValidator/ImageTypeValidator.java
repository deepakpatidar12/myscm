package com.myScm.scm.customValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class ImageTypeValidator implements ConstraintValidator<imageType, MultipartFile>{

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {

        if(value.isEmpty()){
            return true;
        }
        
        return "image/jpeg".equals(value.getContentType()) || "image/png".equals(value.getContentType());
    }
    
}
