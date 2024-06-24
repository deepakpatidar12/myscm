package com.myScm.scm.errorHandler;


import com.myScm.scm.Dto.UserForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataDuplicacyException extends RuntimeException {
    
    private UserForm userForm;

    public DataDuplicacyException(String msg , UserForm userForm) {
        super(msg);
        this.userForm = userForm;
    }


   

}
