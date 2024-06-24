package com.myScm.scm.errorHandler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.myScm.scm.helper.Message;
import com.myScm.scm.helper.MessageType;
import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalExceptionHandler {

    ModelAndView model;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView ResourceNotFoundExceptionhandler(ResourceNotFoundException exception) {

        model = new ModelAndView();

        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience for this");
        model.addObject("message", exception.getMessage());
        model.addObject("statusCode", "404");
        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(ArithmeticException.class)
    public ModelAndView handleArithmeticException(ArithmeticException e) {

        model = new ModelAndView();
        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience");
        model.addObject("message", e.getMessage());
        model.addObject("statusCode", "BAD_REQUEST");

        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(EmailMessageingException.class)
    public ModelAndView handleEmailMessageingException(EmailMessageingException exception) {

        model = new ModelAndView();
        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience ");
        model.addObject("message", exception.getMessage());
        model.addObject("statusCode", "402");

        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(UnExpectedException.class)
    public ModelAndView handleUnExpectedException(UnExpectedException exception) {

        model = new ModelAndView();
        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience ");
        model.addObject("message", exception.getMessage());
        model.addObject("statusCode", "500");

        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(ContactValidationException.class)
    public ModelAndView handleContactValidationException(ContactValidationException exception) {

        model = new ModelAndView();

        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience");
        model.addObject("message", exception.getMessage());
        model.addObject("statusCode", "500");

        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ModelAndView handleContactNotFoundException(ContactNotFoundException exception) {

        model = new ModelAndView();

        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience");
        model.addObject("statusCode", "404");
        model.addObject("message", exception.getMessage());

        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(UserValdationException.class)
    public ModelAndView handleFavoriteContactNotFound(UserValdationException exception) {

        model = new ModelAndView();

        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience");
        model.addObject("statusCode", "402");
        model.addObject("message", exception.getMessage());

        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(UserNotFound.class)
    public ModelAndView handleUserNotFoundException(UserNotFound exception) {

        model = new ModelAndView();

        model.addObject("title", "An unwanted situation privented!");
        model.addObject("error", "sorry for the inconvinience");
        model.addObject("statusCode", "404");
        model.addObject("message", exception.getMessage());

        model.setViewName("error-404");

        return model;
    }

    @ExceptionHandler(DataDuplicacyException.class)
    public ModelAndView handleIntegrityvoilation(HttpSession httpSession, DataDuplicacyException exception) {

        ModelAndView model = new ModelAndView();

        model.addObject("userForm", exception.getUserForm());

        if (exception.getMessage().contains(exception.getUserForm().getUserEmail())) {

            httpSession.setAttribute("message", new Message("User Email already registered!! ", MessageType.red));

        } else if (exception.getMessage().contains(exception.getUserForm().getUserNumber())) {

            httpSession.setAttribute("message", new Message("Mobile Number already Registered!! ", MessageType.red));
        } else {

            httpSession.setAttribute("message",
                    new Message("INTERNAL_SERVER_ERROR try again later!! ", MessageType.red));
        }

        model.addObject("title", "INTERNAL_SERVER_ERROR");
        model.addObject("error", "some unwanted cause prevented try again later some time");

        model.setViewName("signup");

        return model;

    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception exception) {

        model = new ModelAndView();

        model.addObject("title", "INTERNAL_SERVER_ERROR");
        model.addObject("error", "sorry for the inconvinience error please try again later some time");
        model.addObject("message", "try again later " + exception.getMessage().substring(0, 57) + "...");
        model.addObject("statusCode", "500");

        model.setViewName("error-404");

        return model;
    }

}
